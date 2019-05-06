package com.deltadental.platform.elastic.entity;

public class ResponseDetails {

	
	private int totalNumberOfRecords;
	private String message;
	
	public int getTotalNumberOfRecords() {
		return totalNumberOfRecords;
	}
	public void setTotalNumberOfRecords(int totalNumberOfRecords) {
		this.totalNumberOfRecords = totalNumberOfRecords;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

    
}
