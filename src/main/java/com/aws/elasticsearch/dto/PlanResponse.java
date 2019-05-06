package com.aws.elasticsearch.dto;

import com.aws.elasticsearch.entity.PlanDetails;

public class PlanResponse {
	
	private PlanDetails plan;
	private long score;
	private  String id;
	
	public PlanDetails getPlan() {
		return plan;
	}
	public void setPlan(PlanDetails plan) {
		this.plan = plan;
	}
	public long getScore() {
		return score;
	}
	public void setScore(long score) {
		this.score = score;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	

}
