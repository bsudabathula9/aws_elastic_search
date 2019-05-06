package com.aws.elasticsearch.utility;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.aws.elasticsearch.serviceImpl.PlanService;
import com.deltadental.platform.elastic.entity.PlanDetails;

@Component
public class ElasticSearchWriter implements ItemWriter<PlanDetails> {


	private static final Logger logger = LoggerFactory.getLogger(ElasticSearchWriter.class.getName());


	@Autowired
	PlanService planservice;


	@Override
	public void write(List<? extends PlanDetails> items) throws Exception {
		planservice.uploadBulkbatch(items);
	}









}
