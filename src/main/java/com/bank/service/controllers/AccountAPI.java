package com.bank.service.controllers;

import com.bank.service.model.AccountPayLoad;

import javax.ws.rs.core.Response;
import java.util.List;

/**
 * This interface declares common methods of AccountPayLoad operations.
 *
 * @author : Sandeep M
 */

public interface AccountAPI {

    /**
     * List all the accounts, Can be restricted with access and authorization
     * @return : Account List payload
     */
    List<AccountPayLoad> getAllAccounts();

    /**
     * Credit amount into a Account
     * @param accountPayLoad : CustomerPayLoad Made a request
     * @return : Successful or failed.
     */

    Response credit(AccountPayLoad accountPayLoad);


    /**
     * Credit amount into a Account
     * @param accountPayLoad : From Account
     * @return : Successful or failed.
     */

    Response debit(AccountPayLoad accountPayLoad);

    /**
     * Check current Account Balance.
     * @param fromAccount : Source account
     * @param userId : CustomerPayLoad has a made request
     * @return : Response with balance amount
     */

   /* Response checkBalance(String fromAccount, String userId);*/
}
