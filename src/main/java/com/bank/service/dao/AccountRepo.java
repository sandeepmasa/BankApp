package com.bank.service.dao;

import com.bank.service.exception.InsufficientBalanceException;
import com.bank.service.model.AccountPayLoad;

import java.math.BigDecimal;
import java.util.List;


/**
 * segregates  methods of Account operations
 *
 */
public interface AccountRepo {

    List<AccountPayLoad> getAllAccounts() throws InsufficientBalanceException;
    AccountPayLoad getAccountById(long accountId) throws InsufficientBalanceException;
    long createAccount(AccountPayLoad accountPayLoad) throws InsufficientBalanceException;
    int deleteAccountById(long accountId) throws InsufficientBalanceException;

    int credit(long accountId, BigDecimal amount);
    int debit(long accountId, BigDecimal amount);
}
