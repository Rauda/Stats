package com.saurabh.stats.dto;

public class TransactionDto {

	private double amount;
	private long timestamp;

	public TransactionDto() {

	}

	public TransactionDto(double amount, long timestamp) {
		this.amount = amount;
		this.timestamp = timestamp;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

}
