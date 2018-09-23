package com.abc.ledger.enums;

/**
 * The class contains different types of account
 *
 */
public enum AccountType {

	INTERNAL("I"), EXTERNAL("E");

	private String value;

	AccountType(final String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
