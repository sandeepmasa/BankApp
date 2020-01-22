package com.bank.service.service;

import com.bank.service.dao.DAOFactory;
import com.bank.service.exception.InsufficientBalanceException;
import com.bank.service.exception.InvalidAccountException;
import com.bank.service.model.AccountPayLoad;
import lombok.extern.java.Log;

import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Account Service Class implements methods of Account domain class
 *
 * @author : Sandeep M
 */
@Log
public class AccountService {

    private final DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.H2);

    public List<AccountPayLoad> getAllAccounts() {
        return daoFactory.getAccountDAO().getAllAccounts();
    }

    public AccountPayLoad getAccount(long accountId) {
        return daoFactory.getAccountDAO().getAccountById(accountId);
    }

    public AccountPayLoad register(AccountPayLoad accountPayLoad) {
        final long accountId = daoFactory.getAccountDAO().createAccount(accountPayLoad);
        return daoFactory.getAccountDAO().getAccountById(accountId);
    }

    public AccountPayLoad credit() throws InvalidAccountException {
        return null;
    }

    public AccountPayLoad debit() throws InsufficientBalanceException {

        return null;
    }

    public Response unregister(long accountId) throws InvalidAccountException {
        return null;
    }

}
