package com.bank.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class TransactionPayLoad {

	@JsonProperty(required = true)
	private String currencyCode;

	@JsonProperty(required = true)
	private BigDecimal amount;

	@JsonProperty(required = true)
	private Long fromAccountId;

	@JsonProperty(required = true)
	private Long toAccountId;

	public TransactionPayLoad() {
	}

	public TransactionPayLoad(String currencyCode, BigDecimal amount, Long fromAccountId, Long toAccountId) {
		this.currencyCode = currencyCode;
		this.amount = amount;
		this.fromAccountId = fromAccountId;
		this.toAccountId = toAccountId;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public Long getFromAccountId() {
		return fromAccountId;
	}

	public Long getToAccountId() {
		return toAccountId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		TransactionPayLoad that = (TransactionPayLoad) o;

		if (!currencyCode.equals(that.currencyCode))
			return false;
		if (!amount.equals(that.amount))
			return false;
		if (!fromAccountId.equals(that.fromAccountId))
			return false;
		return toAccountId.equals(that.toAccountId);

	}

	@Override
	public int hashCode() {
		int result = currencyCode.hashCode();
		result = 31 * result + amount.hashCode();
		result = 31 * result + fromAccountId.hashCode();
		result = 31 * result + toAccountId.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "TransactionPayLoad{" + "currencyCode='" + currencyCode + '\'' + ", amount=" + amount + ", fromAccountId="
				+ fromAccountId + ", toAccountId=" + toAccountId + '}';
	}

}
