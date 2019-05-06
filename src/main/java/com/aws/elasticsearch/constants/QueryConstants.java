package com.aws.elasticsearch.constants;

public enum QueryConstants {
	
	PLAN_NAME("plan_NAME"),
	SPONSOR_NAME("sponsor_DFE_NAME"),
	SPONSOR_STATE("spons_DFE_MAIL_US_STATE");
	
    private String fieldName;


	QueryConstants(String fieldName){
		this.fieldName = fieldName;
	}


	public String getFieldName() {
		return fieldName;
	}


	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
}
