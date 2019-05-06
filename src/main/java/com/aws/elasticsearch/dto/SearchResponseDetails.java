package com.aws.elasticsearch.dto;

import java.util.List;

public class SearchResponseDetails {
	
	private List<PlanResponse> plans;
	
	private long totalRecords;
	
	private double maxScore;

	

	public long getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(long totalRecords) {
		this.totalRecords = totalRecords;
	}

	public double getMaxScore() {
		return maxScore;
	}

	public void setMaxScore(double maxScore) {
		this.maxScore = maxScore;
	}

	public List<PlanResponse> getPlans() {
		return plans;
	}

	public void setPlans(List<PlanResponse> plans) {
		this.plans = plans;
	}
	
	

}
