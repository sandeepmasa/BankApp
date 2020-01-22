package com.bank.service.controllers;

import com.bank.service.model.CustomerPayLoad;
import com.bank.service.model.TransactionPayLoad;
import com.bank.service.service.BankService;
import com.bank.service.utils.Validator;
import lombok.extern.java.Log;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Objects;



@Path("/bank")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Log
public class BankController implements BankApi {

    private BankService bankService = new BankService();

    @Override
    @POST
    public Response transfer(TransactionPayLoad transactionPayLoad) {
        log.entering("AccountController", "Transfer");
        Objects.requireNonNull(transactionPayLoad, "TransactionPayLoad cannot be null");
        Validator.validateAmountNotNegative(transactionPayLoad.getAmount());

        try{
            bankService.moneyTransfer(transactionPayLoad);
        }catch (Exception ex){
            ex.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ex.getMessage())
                    .build();
        }

        log.exiting("AccountController", "Transfer");

        return Response.status(Response.Status.OK)
                .entity("Successful")
                .build();
    }

    @Override
    public Response register(CustomerPayLoad customerPayLoad) {
        return null;
    }

    @Override
    public Response unRegister(long customerId) {
        return null;
    }
}
