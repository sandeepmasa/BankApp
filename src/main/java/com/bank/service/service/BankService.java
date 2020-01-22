package com.bank.service.service;

import com.bank.service.dao.DAOFactory;
import com.bank.service.exception.InsufficientBalanceException;
import com.bank.service.exception.InvalidAccountException;
import com.bank.service.model.AccountPayLoad;
import com.bank.service.model.TransactionPayLoad;
import com.bank.service.utils.Validator;

import java.util.Objects;

/**
 * This Class implements the methods of BankAPIs
 *
 * @author : Sandeep M
 */

public class BankService {

    private static final Object tieLock = new Object();
    private final DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.H2);

    public boolean moneyTransfer(TransactionPayLoad transactionPayLoad) throws InsufficientBalanceException, InvalidAccountException {

        AccountPayLoad fromAccountPayLoad = daoFactory.getAccountDAO().getAccountById(transactionPayLoad.getFromAccountId());
        AccountPayLoad toAccountPayLoad = daoFactory.getAccountDAO().getAccountById(transactionPayLoad.getToAccountId());

        Objects.requireNonNull(fromAccountPayLoad, "Invalid FROM Account");
        Objects.requireNonNull(toAccountPayLoad, "Invalid TO Account");
        Validator.validateCurrency(fromAccountPayLoad, toAccountPayLoad, transactionPayLoad.getCurrencyCode());
        Validator.validateSameAccount(fromAccountPayLoad, toAccountPayLoad);

        class Helper {
            public void transfer() throws InsufficientBalanceException {
                if (fromAccountPayLoad.getBalance().compareTo(transactionPayLoad.getAmount()) < 0)
                    throw new InsufficientBalanceException(" Insufficient Balance");
                else {
                    int success = daoFactory.getAccountDAO().debit(fromAccountPayLoad.getAccountNumber(), transactionPayLoad.getAmount());
                    if (success > 0) {
                        int count = daoFactory.getAccountDAO().credit(toAccountPayLoad.getAccountNumber(), transactionPayLoad.getAmount());

                        if (count < 1) {
                            daoFactory.getAccountDAO().credit(fromAccountPayLoad.getAccountNumber(), transactionPayLoad.getAmount());
                        }

                    }
                }
            }
        }


        int fromHash = System.identityHashCode(fromAccountPayLoad);
        int toHash = System.identityHashCode(toAccountPayLoad);

        if (fromHash < toHash) {
            synchronized (fromAccountPayLoad) {
                synchronized (toAccountPayLoad) {
                    new Helper().transfer();
                }
            }
        } else if (fromHash > toHash) {
            synchronized (toAccountPayLoad) {
                synchronized (fromAccountPayLoad) {
                    new Helper().transfer();
                }
            }
        } else {
            synchronized (tieLock) {
                synchronized (fromAccountPayLoad) {
                    synchronized (toAccountPayLoad) {
                        new Helper().transfer();
                    }
                }
            }
        }
        return true;

    }

}
