package com.bankApp.test.services;


import com.bank.service.model.TransactionPayLoad;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertTrue;

public class BankServiceTest extends TestService {

    @Test
    public void whenNotEngoughFundsErrorCode400() throws IOException, URISyntaxException {
        h2DaoFactory.LoadTestData();
        URI uri = builder.setPath("/bank").build();
        BigDecimal amount = new BigDecimal(1000).setScale(4, RoundingMode.HALF_EVEN);
        TransactionPayLoad transactionPayLoad = new TransactionPayLoad("USD", amount, 1L, 2L);

        String jsonInString = mapper.writeValueAsString(transactionPayLoad);
        StringEntity entity = new StringEntity(jsonInString);
        HttpPost request = new HttpPost(uri);
        request.setHeader("Content-type", "application/json");
        request.setEntity(entity);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertTrue(statusCode == 400);
    }

    @Test
    public void whenTransferWithDifferentCurrencyThenErrorCode400() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/bank").build();
        BigDecimal amount = new BigDecimal(100).setScale(4, RoundingMode.HALF_EVEN);
        TransactionPayLoad transactionPayLoad = new TransactionPayLoad("INR", amount, 1L, 2L);

        String jsonInString = mapper.writeValueAsString(transactionPayLoad);
        StringEntity entity = new StringEntity(jsonInString);
        HttpPost request = new HttpPost(uri);
        request.setHeader("Content-type", "application/json");
        request.setEntity(entity);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("STCODE" + statusCode);
        assertTrue(statusCode == 400);

    }


}
