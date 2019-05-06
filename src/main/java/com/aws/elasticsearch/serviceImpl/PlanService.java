package com.aws.elasticsearch.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aws.elasticsearch.dto.PlanResponse;
import com.aws.elasticsearch.dto.SearchResponseDetails;
import com.aws.elasticsearch.entity.PlanDetails;
import com.aws.elasticsearch.repo.PlanRepository;
import com.aws.elasticsearch.service.IPlanService;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class PlanService implements IPlanService {


	@Autowired
	PlanRepository planRepository;

	@Autowired
	ObjectMapper objectMapper;

	@Override
	public PlanDetails getPlanDetails(String planId) throws Exception {

		return 	planRepository.findById(planId);

	}



	public void uploadBulkbatch(List<? extends PlanDetails> plansList) throws Exception{
	
		planRepository.saveAll(plansList);
		System.out.println("Plan objects are indexed successfully : "+plansList.size() );
		
	}

	@Override
	public SearchResponseDetails searchPlanDetails(String serachBy, String queryText) throws Exception {
		SearchResponseDetails searchResponse = new SearchResponseDetails();
		SearchResponse response = planRepository.findAll(serachBy,queryText);
		List<PlanResponse> plan = new ArrayList<>();
		PlanResponse planResponse =null;
		
		if(null != response && response.getHits().totalHits > 0) {
			searchResponse.setTotalRecords(response.getHits().totalHits);
			SearchHit[] results = response.getHits().getHits();
		
			for (SearchHit hit : results) {
				Map<String, Object> result = hit.getSourceAsMap();
				planResponse = new PlanResponse();
				planResponse.setScore((long)hit.getScore());
				planResponse.setId(hit.getId());
				planResponse.setPlan(objectMapper.convertValue(result, PlanDetails.class));
				plan.add(planResponse);

			}

		}
		searchResponse.setPlans(plan);
		searchResponse.setMaxScore(response.getHits().getMaxScore());
		return searchResponse;
		
	}



}
