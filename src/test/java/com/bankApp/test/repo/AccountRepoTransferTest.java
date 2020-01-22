package com.bankApp.test.repo;

import com.bank.service.dao.DAOFactory;
import com.bank.service.exception.InsufficientBalanceException;
import com.bank.service.model.AccountPayLoad;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

public class AccountRepoTransferTest {

	private static final DAOFactory h2DaoFactory = DAOFactory.getDAOFactory(DAOFactory.H2);

	@BeforeClass
	public static void setup() {
		// prepare test database and test data. Test data are initialised from
		// src/test/resources/demo.sql
		h2DaoFactory.LoadTestData();
	}

	@After
	public void tearDown() {

	}

	@Test
	public void testGetAllAccounts() throws InsufficientBalanceException {
		List<AccountPayLoad> allAccountPayLoads = h2DaoFactory.getAccountDAO().getAllAccounts();
		assertTrue(allAccountPayLoads.size() > 1);
	}

	@Test
	public void testGetAccountById() throws InsufficientBalanceException {
		AccountPayLoad accountPayLoad = h2DaoFactory.getAccountDAO().getAccountById(1L);
		assertTrue(accountPayLoad.getCustomerId().equals("11"));
	}

	@Test
	public void testGetNonExistingAccById() throws InsufficientBalanceException {
		AccountPayLoad accountPayLoad = h2DaoFactory.getAccountDAO().getAccountById(100L);
		assertTrue(accountPayLoad == null);
	}

	@Test
	public void testCreateAccount() throws InsufficientBalanceException {
		BigDecimal balance = new BigDecimal(10).setScale(4, RoundingMode.HALF_EVEN);
		AccountPayLoad payLoad = new AccountPayLoad(11, "51", balance, "USD");
		long aid = h2DaoFactory.getAccountDAO().createAccount(payLoad);
		AccountPayLoad afterCreation = h2DaoFactory.getAccountDAO().getAccountById(aid);
		assertTrue(afterCreation.getCustomerId().equals("51"));
		assertTrue(afterCreation.getCurrencyCode().equals("USD"));
		assertTrue(afterCreation.getBalance().equals(balance));
	}

	@Test
	public void testDeleteAccount() throws InsufficientBalanceException {
		int rowCount = h2DaoFactory.getAccountDAO().deleteAccountById(2L);
		// assert one row(user) deleted
		assertTrue(rowCount == 1);
		// assert user no longer there
		assertTrue(h2DaoFactory.getAccountDAO().getAccountById(2L) == null);
	}

	@Test
	public void testDeleteNonExistingAccount() throws InsufficientBalanceException {
		int rowCount = h2DaoFactory.getAccountDAO().deleteAccountById(500L);
		// assert no row(user) deleted
		assertTrue(rowCount == 0);

	}

	@Test(expected = InsufficientBalanceException.class)
	public void testUpdateAccountBalanceNotEnoughFund() throws InsufficientBalanceException {
		BigDecimal deltaWithDraw = new BigDecimal(50000).setScale(4, RoundingMode.HALF_EVEN);
		int rowsUpdatedW = h2DaoFactory.getAccountDAO().debit(1L, deltaWithDraw);
	}

}