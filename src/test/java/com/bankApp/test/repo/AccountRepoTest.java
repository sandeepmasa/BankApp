package com.bankApp.test.repo;

import com.bank.service.dao.AccountRepo;
import com.bank.service.dao.DAOFactory;
import com.bank.service.exception.InsufficientBalanceException;
import com.bank.service.model.AccountPayLoad;
import com.bank.service.model.TransactionPayLoad;

import com.bank.service.service.BankService;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.CountDownLatch;

import static junit.framework.TestCase.assertTrue;

public class AccountRepoTest {

	private static Logger log = Logger.getLogger(AccountRepoTransferTest.class);
	private static final DAOFactory h2DaoFactory = DAOFactory.getDAOFactory(DAOFactory.H2);
	private static final int THREADS_COUNT = 100;

	@BeforeClass
	public static void setup() {
		// prepare test database and test data, Test data are initialised from
		// src/test/resources/demo.sql
		h2DaoFactory.LoadTestData();
	}

	@After
	public void tearDown() {

	}

	@Test
	public void whenAccountTransferIs100USDthenAccountDebit() throws InsufficientBalanceException {

		final AccountRepo accountRepo = h2DaoFactory.getAccountDAO();
		BankService bankService = new BankService();


		AccountPayLoad accountPayLoadFrom = accountRepo.getAccountById(1);

		AccountPayLoad accountPayLoadTo = accountRepo.getAccountById(2);

		BigDecimal originalAmt = accountPayLoadFrom.getBalance();

		accountRepo.credit(accountPayLoadFrom.getAccountNumber(),new BigDecimal(100.00).setScale(2, RoundingMode.HALF_EVEN));

		originalAmt = new BigDecimal(100.00).setScale(2, RoundingMode.HALF_EVEN).add(accountPayLoadFrom.getBalance());

		BigDecimal transferAmount = new BigDecimal(100.00).setScale(2, RoundingMode.HALF_EVEN);

		BigDecimal creditAcctAmt = accountPayLoadTo.getBalance().add(transferAmount);
		BigDecimal debitAcctAmt = accountPayLoadFrom.getBalance().subtract(transferAmount);

		TransactionPayLoad transactionPayLoad = new TransactionPayLoad("USD", transferAmount, 1L, 2L);

		long startTime = System.currentTimeMillis();

		bankService.moneyTransfer(transactionPayLoad);
		long endTime = System.currentTimeMillis();

		log.info("TransferAccountBalance finished, time taken: " + (endTime - startTime) + "ms");

		accountPayLoadFrom = accountRepo.getAccountById(1);

		accountPayLoadTo = accountRepo.getAccountById(2);

		log.debug("AccountPayLoad From: " + accountPayLoadFrom);

		log.debug("AccountPayLoad From: " + accountPayLoadTo);

		assertTrue(
				accountPayLoadFrom.getBalance().compareTo(new BigDecimal(100.00).setScale(2, RoundingMode.HALF_EVEN)) == 0);
		assertTrue(accountPayLoadTo.getBalance().equals(creditAcctAmt.setScale(4, RoundingMode.HALF_EVEN)));

	}

	@Test
	public void whenmultiRequestsTransferDoTransfer() throws InterruptedException, InsufficientBalanceException {
		final AccountRepo accountRepo = h2DaoFactory.getAccountDAO();
		h2DaoFactory.LoadTestData();
		// transfer a total of 200USD from 100USD balance in multi-threaded
		// mode, expect half of the transaction fail
		final CountDownLatch latch = new CountDownLatch(THREADS_COUNT);
		for (int i = 0; i < THREADS_COUNT; i++) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						TransactionPayLoad transactionPayLoad = new TransactionPayLoad("USD",
								new BigDecimal(2).setScale(4, RoundingMode.HALF_EVEN), 1L, 2L);
						new BankService().moneyTransfer(transactionPayLoad);
					} catch (Exception e) {
						log.error("Error occurred during transfer ", e);
					} finally {
						latch.countDown();
					}
				}
			}).start();
		}

		latch.await();

		AccountPayLoad accountPayLoadFrom = accountRepo.getAccountById(1);

		AccountPayLoad accountPayLoadTo = accountRepo.getAccountById(2);

		log.debug("AccountPayLoad From: " + accountPayLoadFrom);

		log.debug("AccountPayLoad From: " + accountPayLoadTo);

		assertTrue(accountPayLoadFrom.getBalance().equals(new BigDecimal(0).setScale(4, RoundingMode.HALF_EVEN)));
		assertTrue(accountPayLoadTo.getBalance().equals(new BigDecimal(300).setScale(4, RoundingMode.HALF_EVEN)));

	}

}
