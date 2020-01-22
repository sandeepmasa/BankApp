package com.bank.service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * Model Class defines account fields and common methods.
 *
 * @author : Sandeep M
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode
public class AccountPayLoad implements Serializable {

    @JsonProperty(required = false)
    private long accountNumber;

    @JsonProperty(required = true)
    private String customerId;

    @JsonProperty(required = true)
    private BigDecimal balance;

    @JsonProperty(required = true)
    private String currencyCode;

    public AccountPayLoad(){}

}
