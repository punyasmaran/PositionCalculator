package com.abc.ledger.pojo;

/**
 * This class contains positions for the instrument
 *
 */
public class Position {

	private String instrument;
	private String account;
	private String accType;
	private Long quantity;
	private Long netVoluume;

	public String getInstrument() {
		return instrument;
	}

	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAccType() {
		return accType;
	}

	public void setAccType(String accType) {
		this.accType = accType;
	}

	public long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public Long getNetVoluume() {
		return netVoluume;
	}

	public void setNetVoluume(Long netVoluume) {
		this.netVoluume = netVoluume;
	}
}
