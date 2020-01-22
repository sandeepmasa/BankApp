package com.bank.service.dao.impl;

import com.bank.service.dao.H2DAOFactory;
import com.bank.service.dao.CustomerRepo;
import com.bank.service.exception.InsufficientBalanceException;
import com.bank.service.model.CustomerPayLoad;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class CustomerRepository implements CustomerRepo {
	
    private static Logger log = Logger.getLogger(CustomerRepository.class);
    private final static String SQL_GET_USER_BY_ID = "SELECT * FROM CustomerPayLoad WHERE UserId = ? ";
    private final static String SQL_GET_ALL_USERS = "SELECT * FROM CustomerPayLoad";
    private final static String SQL_GET_USER_BY_NAME = "SELECT * FROM CustomerPayLoad WHERE UserName = ? ";
    private final static String SQL_INSERT_USER = "INSERT INTO CustomerPayLoad (UserName, EmailAddress) VALUES (?, ?)";
    private final static String SQL_UPDATE_USER = "UPDATE CustomerPayLoad SET UserName = ?, EmailAddress = ? WHERE UserId = ? ";
    private final static String SQL_DELETE_USER_BY_ID = "DELETE FROM CustomerPayLoad WHERE UserId = ? ";
    
    /**
     * Find all users
     */
    public List<CustomerPayLoad> getAllUsers() throws InsufficientBalanceException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<CustomerPayLoad> customerPayLoads = new ArrayList<CustomerPayLoad>();
        try {
            conn = H2DAOFactory.getConnection();
            stmt = conn.prepareStatement(SQL_GET_ALL_USERS);
            rs = stmt.executeQuery();
            while (rs.next()) {
                CustomerPayLoad u = new CustomerPayLoad(rs.getLong("UserId"), rs.getString("UserName"), rs.getString("EmailAddress"));
                customerPayLoads.add(u);
                if (log.isDebugEnabled())
                    log.debug("getAllUsers() Retrieve CustomerPayLoad: " + u);
            }
            return customerPayLoads;
        } catch (SQLException e) {
            throw new InsufficientBalanceException("Error reading user data", e);
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
    }
    
    /**
     * Find user by userId
     */
    public CustomerPayLoad getUserById(long userId) throws InsufficientBalanceException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        CustomerPayLoad u = null;
        try {
            conn = H2DAOFactory.getConnection();
            stmt = conn.prepareStatement(SQL_GET_USER_BY_ID);
            stmt.setLong(1, userId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                u = new CustomerPayLoad(rs.getLong("UserId"), rs.getString("UserName"), rs.getString("EmailAddress"));
                if (log.isDebugEnabled())
                    log.debug("getUserById(): Retrieve CustomerPayLoad: " + u);
            }
            return u;
        } catch (SQLException e) {
            throw new InsufficientBalanceException("Error reading user data", e);
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
    }
    
    /**
     * Find user by userName
     */
    public CustomerPayLoad getUserByName(String userName) throws InsufficientBalanceException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        CustomerPayLoad u = null;
        try {
            conn = H2DAOFactory.getConnection();
            stmt = conn.prepareStatement(SQL_GET_USER_BY_NAME);
            stmt.setString(1, userName);
            rs = stmt.executeQuery();
            if (rs.next()) {
                u = new CustomerPayLoad(rs.getLong("UserId"), rs.getString("UserName"), rs.getString("EmailAddress"));
                if (log.isDebugEnabled())
                    log.debug("Retrieve CustomerPayLoad: " + u);
            }
            return u;
        } catch (SQLException e) {
            throw new InsufficientBalanceException("Error reading user data", e);
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
    }
    
    /**
     * Save CustomerPayLoad
     */
    public long insertUser(CustomerPayLoad customerPayLoad) throws InsufficientBalanceException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet generatedKeys = null;
        try {
            conn = H2DAOFactory.getConnection();
            stmt = conn.prepareStatement(SQL_INSERT_USER, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, customerPayLoad.getName());
            stmt.setString(2, customerPayLoad.getEmailAddress());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                log.error("insertUser(): Creating customerPayLoad failed, no rows affected." + customerPayLoad);
                throw new InsufficientBalanceException("Users Cannot be created");
            }
            generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getLong(1);
            } else {
                log.error("insertUser():  Creating customerPayLoad failed, no ID obtained." + customerPayLoad);
                throw new InsufficientBalanceException("Users Cannot be created");
            }
        } catch (SQLException e) {
            log.error("Error Inserting CustomerPayLoad :" + customerPayLoad);
            throw new InsufficientBalanceException("Error creating customerPayLoad data", e);
        } finally {
            DbUtils.closeQuietly(conn,stmt,generatedKeys);
        }

    }
    
    /**
     * Update CustomerPayLoad
     */
    public int updateUser(Long userId,CustomerPayLoad customerPayLoad) throws InsufficientBalanceException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = H2DAOFactory.getConnection();
            stmt = conn.prepareStatement(SQL_UPDATE_USER);
            stmt.setString(1, customerPayLoad.getName());
            stmt.setString(2, customerPayLoad.getEmailAddress());
            stmt.setLong(3, userId);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error Updating CustomerPayLoad :" + customerPayLoad);
            throw new InsufficientBalanceException("Error update customerPayLoad data", e);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(stmt);
        }
    }
    
    /**
     * Delete CustomerPayLoad
     */
    public int deleteUser(long userId) throws InsufficientBalanceException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = H2DAOFactory.getConnection();
            stmt = conn.prepareStatement(SQL_DELETE_USER_BY_ID);
            stmt.setLong(1, userId);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error Deleting CustomerPayLoad :" + userId);
            throw new InsufficientBalanceException("Error Deleting CustomerPayLoad ID:"+ userId, e);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(stmt);
        }
    }

}
