package com.bank.service.utils;

import com.bank.service.model.AccountPayLoad;

import java.math.BigDecimal;
import java.util.Objects;

public class Validator {

    public static void validateAmountNotNegative(BigDecimal amount) {
        Objects.requireNonNull(amount, "Amount cannot be null");
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
    }

    public static void validateCurrency(AccountPayLoad fromAcct, AccountPayLoad toAcct, String currencyTransfer) {
        if (!fromAcct.getCurrencyCode().equals(toAcct.getCurrencyCode())) {
            throw new UnsupportedOperationException(
                    String.format("Multi-currency operations are not supported. Debit account = %s; credit account = %s", fromAcct.getCurrencyCode(), toAcct.getCurrencyCode()));
        }
        if (!fromAcct.getCurrencyCode().equals(currencyTransfer)) {
            throw new UnsupportedOperationException(
                    String.format("Multi-currency operations are not supported. Account currency and Transfer currency should be same %s, %s", fromAcct.getCurrencyCode(), currencyTransfer));
        }
    }

    public static void validateSameAccount(AccountPayLoad fromAcct, AccountPayLoad toAcct) {
        if (fromAcct.getAccountNumber() == toAcct.getAccountNumber()) {
            throw new UnsupportedOperationException(
                    String.format("Same account Transfer not supported. Please use Debit/Credit APIs"));
        }
    }
}
