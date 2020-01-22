package com.bank.service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Customer payload Model
 *
 * @author : Sandeep M
 */

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class CustomerPayLoad {

    @JsonIgnore
    private long userId;

    @JsonProperty(required = true)
    private String name;


    @JsonProperty(required = true)
    private String emailAddress;

    public CustomerPayLoad() {
    }

    public CustomerPayLoad(String name, String emailAddress) {
        this.name = name;
        this.emailAddress = emailAddress;
    }

}
