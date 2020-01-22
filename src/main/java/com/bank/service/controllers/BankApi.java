package com.bank.service.controllers;

import com.bank.service.model.CustomerPayLoad;
import com.bank.service.model.TransactionPayLoad;

import javax.ws.rs.core.Response;

/**
 * This Controller implements the BankAPIs and provide Response messages.
 *
 * This Class can be generated from Java plugins and can be exported to Clients
 *
 * @author : Sandeep M
 */


public interface BankApi {

    /**
     * Transfer from a Account to another account,
     * @param transactionPayLoad : Format that client should be called with body message
     * @return : Success or Failed
     */
    Response transfer(TransactionPayLoad transactionPayLoad);

    /**
     * Register new CustomerPayLoad with Bank
     * @param customerPayLoad : CustomerPayLoad object
     * @return : Success or Failed
     */
    Response register(CustomerPayLoad customerPayLoad);

    /**
     * UnRegister new CustomerPayLoad with Bank
     * @param customerId : CustomerPayLoad object
     * @return : Success or Failed
     */
    Response unRegister(long customerId);
}
