package com.abc.ledger.pojo;

/**
 * This class contains transactions for the day
 *
 */
public class TransactionDetails {

	private int transactionId;
	private String instrument;
	private String transactionType;
	private long transactionQuantity;

	public int getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	public String getInstrument() {
		return instrument;
	}

	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public long getTransactionQuantity() {
		return transactionQuantity;
	}

	public void setTransactionQuantity(long transactionQuantity) {
		this.transactionQuantity = transactionQuantity;
	}
}
