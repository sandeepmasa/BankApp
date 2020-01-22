package com.bank.service.dao.impl;

import com.bank.service.dao.AccountRepo;
import com.bank.service.dao.H2DAOFactory;
import com.bank.service.model.AccountPayLoad;
import com.bank.service.exception.InsufficientBalanceException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AccountRepository implements AccountRepo {

	private static Logger log = Logger.getLogger(AccountRepository.class);
	private final static String SQL_GET_ACC_BY_ID = "SELECT * FROM Account WHERE AccountId = ? ";
	private final static String SQL_LOCK_ACC_BY_ID = "SELECT * FROM Account WHERE AccountId = ? FOR UPDATE";
	private final static String SQL_CREATE_ACC = "INSERT INTO Account (CustomerId, Balance, CurrencyCode) VALUES (?, ?, ?)";
	private final static String SQL_UPDATE_ACC_BALANCE = "UPDATE Account SET Balance = ? WHERE AccountId = ? ";
	private final static String SQL_GET_ALL_ACC = "SELECT * FROM Account";
	private final static String SQL_DELETE_ACC_BY_ID = "DELETE FROM Account WHERE AccountId = ?";
	
	/**
	 * Get all accounts.
	 */
	public List<AccountPayLoad> getAllAccounts() throws InsufficientBalanceException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<AccountPayLoad> allAccountPayLoads = new ArrayList();
		try {
			conn = H2DAOFactory.getConnection();
			stmt = conn.prepareStatement(SQL_GET_ALL_ACC);
			rs = stmt.executeQuery();
			while (rs.next()) {
				AccountPayLoad acc = new AccountPayLoad(rs.getLong("AccountId"), rs.getString("CustomerId"),
						rs.getBigDecimal("Balance"), rs.getString("CurrencyCode"));
				if (log.isDebugEnabled())
					log.debug("getAllAccounts(): Get  AccountPayLoad " + acc);
				allAccountPayLoads.add(acc);
			}
			return allAccountPayLoads;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new InsufficientBalanceException("getAccountById(): Error reading account data", e);
		} finally {
			DbUtils.closeQuietly(conn, stmt, rs);
		}
	}
	
	/**
	 * Get account by id
	 */
	public AccountPayLoad getAccountById(long accountId) throws InsufficientBalanceException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		AccountPayLoad acc = null;
		try {
			conn = H2DAOFactory.getConnection();
			stmt = conn.prepareStatement(SQL_GET_ACC_BY_ID);
			stmt.setLong(1, accountId);
			rs = stmt.executeQuery();
			if (rs.next()) {
				acc = new AccountPayLoad(rs.getLong("AccountId"), rs.getString("CustomerId"), rs.getBigDecimal("Balance"),
						rs.getString("CurrencyCode"));
				if (log.isDebugEnabled())
					log.debug("Retrieve AccountPayLoad By Id: " + acc);
			}
			return acc;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new InsufficientBalanceException("getAccountById(): Error reading account data", e);
		} finally {
			DbUtils.closeQuietly(conn, stmt, rs);
		}

	}
	
	/**
	 * Create accountPayLoad
	 */
	public long createAccount(AccountPayLoad accountPayLoad) throws InsufficientBalanceException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet generatedKeys = null;
		try {
			conn = H2DAOFactory.getConnection();
			stmt = conn.prepareStatement(SQL_CREATE_ACC);
			stmt.setString(1, accountPayLoad.getCustomerId());
			stmt.setBigDecimal(2, accountPayLoad.getBalance());
			stmt.setString(3, accountPayLoad.getCurrencyCode());
			int affectedRows = stmt.executeUpdate();
			if (affectedRows == 0) {
				log.error("createAccount(): Creating accountPayLoad failed, no rows affected.");
				throw new InsufficientBalanceException("AccountPayLoad Cannot be created");
			}
			generatedKeys = stmt.getGeneratedKeys();
			if (generatedKeys.next()) {
				return generatedKeys.getLong(1);
			} else {
				log.error("Creating accountPayLoad failed, no ID obtained.");
				throw new InsufficientBalanceException("AccountPayLoad Cannot be created");
			}
		} catch (SQLException e) {
			log.error("Error Inserting AccountPayLoad  " + accountPayLoad);
			throw new InsufficientBalanceException("createAccount(): Error creating user accountPayLoad " + accountPayLoad, e);
		} finally {
			DbUtils.closeQuietly(conn, stmt, generatedKeys);
		}
	}
	
	/**
	 * Delete account by id
	 */
	public int deleteAccountById(long accountId) throws InsufficientBalanceException {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = H2DAOFactory.getConnection();
			stmt = conn.prepareStatement(SQL_DELETE_ACC_BY_ID);
			stmt.setLong(1, accountId);
			return stmt.executeUpdate();
		} catch (SQLException e) {
			throw new InsufficientBalanceException("deleteAccountById(): Error deleting user account Id " + accountId, e);
		} finally {
			DbUtils.closeQuietly(conn);
			DbUtils.closeQuietly(stmt);
		}
	}
	
	/**
	 * Update account balance
	 */
	public synchronized int credit(long accountId, BigDecimal deltaAmount)  {
		PreparedStatement lockStmt = null;
		PreparedStatement updateStmt = null;
		ResultSet rs = null;
		AccountPayLoad targetAccountPayLoad = null;
		int updateCount = -1;
		try (Connection conn = H2DAOFactory.getConnection()){

			conn.setAutoCommit(false);
			// lock account for writing:
			lockStmt = conn.prepareStatement(SQL_LOCK_ACC_BY_ID);
			lockStmt.setLong(1, accountId);
			rs = lockStmt.executeQuery();
			if (rs.next()) {
				targetAccountPayLoad = new AccountPayLoad(rs.getLong("AccountId"), rs.getString("CustomerId"),
						rs.getBigDecimal("Balance"), rs.getString("CurrencyCode"));
			}

			if(Objects.isNull(targetAccountPayLoad)){
				log.error("Account not avalable");
				return 0;
			}


			// update account upon success locking
			BigDecimal balance = targetAccountPayLoad.getBalance().add(deltaAmount);

			updateStmt = conn.prepareStatement(SQL_UPDATE_ACC_BALANCE);
			updateStmt.setBigDecimal(1, balance);
			updateStmt.setLong(2, accountId);
			updateCount = updateStmt.executeUpdate();
			conn.commit();

			return updateCount;
		} catch (SQLException ex) {
			ex.printStackTrace();
			log.error("SQL exception",ex);
		} finally {
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(lockStmt);
			DbUtils.closeQuietly(updateStmt);
		}
		return updateCount;
	}


	public synchronized int debit(long accountId, BigDecimal deltaAmount)  {
		PreparedStatement lockStmt = null;
		PreparedStatement updateStmt = null;
		ResultSet rs = null;
		AccountPayLoad targetAccountPayLoad = null;
		int updateCount = -1;
		try (Connection conn = H2DAOFactory.getConnection()){

			conn.setAutoCommit(false);
			// lock account for writing:
			lockStmt = conn.prepareStatement(SQL_LOCK_ACC_BY_ID);
			lockStmt.setLong(1, accountId);
			rs = lockStmt.executeQuery();
			if (rs.next()) {
				targetAccountPayLoad = new AccountPayLoad(rs.getLong("AccountId"), rs.getString("CustomerId"),
						rs.getBigDecimal("Balance"), rs.getString("CurrencyCode"));
			}

			if(Objects.isNull(targetAccountPayLoad)){
				log.error("Account not avalable");
				return 0;
			}


			if(targetAccountPayLoad.getBalance().subtract(deltaAmount).doubleValue() < 0.0d){
				throw new InsufficientBalanceException("Insufficient Balance");
			}

			// update account upon success locking
			BigDecimal balance = targetAccountPayLoad.getBalance().subtract(deltaAmount);

			updateStmt = conn.prepareStatement(SQL_UPDATE_ACC_BALANCE);
			updateStmt.setBigDecimal(1, balance);
			updateStmt.setLong(2, accountId);
			updateCount = updateStmt.executeUpdate();
			conn.commit();

			return updateCount;
		} catch (SQLException ex) {
			ex.printStackTrace();
			log.error("SQL exception",ex);
		} finally {
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(lockStmt);
			DbUtils.closeQuietly(updateStmt);
		}
		return updateCount;
	}
}
