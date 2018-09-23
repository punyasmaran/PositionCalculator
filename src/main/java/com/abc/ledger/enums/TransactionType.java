package com.abc.ledger.enums;

/**
 * This class contains different types of transaction
 *
 */
public enum TransactionType {

	SELL("S"), BUY("B");

	private String value;

	TransactionType(final String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
