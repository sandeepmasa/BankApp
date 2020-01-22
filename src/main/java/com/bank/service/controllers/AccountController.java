package com.bank.service.controllers;

import com.bank.service.exception.InvalidAccountException;
import com.bank.service.model.AccountPayLoad;
import com.bank.service.service.AccountService;
import lombok.extern.java.Log;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.List;

/**
 * This Class implements methods of AccountPayLoad APIs and exposes REST End points
 *
 * @author : Sandeep M
 */

@Path("/account")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Log
public class AccountController implements AccountAPI {

    private AccountService accountService = new AccountService();

    @GET
    @Path("/list")
    public List<AccountPayLoad> getAllAccounts() throws InvalidAccountException {
        return accountService.getAllAccounts();
    }

    @POST
    @Path("/register")
    public AccountPayLoad register(AccountPayLoad accountPayLoad)
    {
        return accountService.register(accountPayLoad);
    }

    @PUT
    @Path("/{accountId}/debit/{amount}")
    public Response credit(AccountPayLoad accountPayLoad) {
        return null;
    }

    @PUT
    @Path("/{accountId}/credit/{amount}")
    public Response debit(AccountPayLoad accountPayLoad) {
        return null;
    }


    @DELETE
    @Path("/{accountId}")
    public Response deleteAccount(@PathParam("accountId") long accountId) {
        return null;

    }

}
