package com.aws.elasticsearch.service;

import java.util.List;

import com.aws.elasticsearch.dto.SearchResponseDetails;
import com.deltadental.platform.elastic.entity.PlanDetails;

public interface IPlanService {


	PlanDetails getPlanDetails(String planId) throws Exception;

	public SearchResponseDetails searchPlanDetails(String serachBy, String queryText) throws Exception;

	public void uploadBulkbatch(List<? extends PlanDetails> items) throws Exception;

}
