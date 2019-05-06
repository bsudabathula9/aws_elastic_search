package com.aws.elasticsearch.repo;

import java.util.List;
import java.util.Map;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.aws.elasticsearch.constants.PlanConstants;
import com.aws.elasticsearch.constants.QueryConstants;
import com.deltadental.platform.elastic.entity.PlanDetails;
import com.fasterxml.jackson.databind.ObjectMapper;


@Component
public class PlanRepository {

	private static final Logger logger = LoggerFactory.getLogger(PlanRepository.class.getName());

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	BulkProcessor bulkProcessor;

	@Autowired
	private RestHighLevelClient client;

	public PlanDetails findById(String planId) throws Exception {

		PlanDetails planInfo = null;
		try {
			GetRequest request = new GetRequest(PlanConstants.INDEX, PlanConstants.TYPE, planId);
			GetResponse response = client.get(request, RequestOptions.DEFAULT);
			planInfo = objectMapper.convertValue(response.getSource(), PlanDetails.class);
		} catch (Exception e) {
			logger.error("Problem occured while fteching the Plan details and the planId is {}", planId);
		}

		return planInfo;

	}

	public void saveAll(List<? extends PlanDetails> plansList) throws Exception {

		try {
			plansList.forEach(item -> {
				IndexRequest indexRequest = new IndexRequest(PlanConstants.INDEX, PlanConstants.TYPE, item.getACK_ID())
						.source(objectMapper.convertValue(item, Map.class));
				bulkProcessor.add(indexRequest);
			});
		} catch (Exception e) {
			logger.error("Problem occured while saving the Plan details and the error is {}", e);
			throw e;
		}

	}

	public SearchResponse findAll(String searchBy, String queryText) throws Exception {

		SearchResponse searchResponse = null;
		try {

			SearchRequest searchRequest = new SearchRequest(PlanConstants.INDEX);
			SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

			MatchQueryBuilder matchQueryBuilder = QueryBuilders
					.matchQuery(QueryConstants.valueOf(searchBy).getFieldName(), queryText).fuzziness(Fuzziness.AUTO)
					.prefixLength(3).maxExpansions(10);
			sourceBuilder.query(matchQueryBuilder);
			searchRequest.source(sourceBuilder);
			searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

		} catch (Exception e) {
			logger.error("Problem occured while fetching the Plan details and the error is {}", e);
			throw e;
		}

		return searchResponse;

	}

}