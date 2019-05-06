package com.aws.elasticsearch.config;

import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkProcessor.Builder;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ElasticsearchConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(ElasticsearchConfig.class.getName());


	@Value("${elasticsearch.host}")
	private String esHost;
	
	private int beforeBulkProcessorCount=1;
	private int afterBulkProcessorCount=1;

	@Bean(destroyMethod = "close")
	public RestHighLevelClient client() {
		RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost(esHost)));
		return client;
	}

	@Bean
	public BulkProcessor bulkProcessor() throws Exception {

		Builder processor =  BulkProcessor.builder(
				(request, bulkListener) -> client().bulkAsync(request, RequestOptions.DEFAULT, bulkListener),
				new BulkProcessor.Listener() {
					@Override
					public void beforeBulk(long executionId, BulkRequest request) {
						logger.info("Before insert this bacth  - beforeBulkProcessorCount :  " +beforeBulkProcessorCount);
						beforeBulkProcessorCount++;
					}

					@Override
					public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
						logger.info("Successfully inserted this bacth - afterBulkProcessorCount: " +afterBulkProcessorCount);
						afterBulkProcessorCount++;

					}

					@Override
					public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
						logger.info("Exception occured while upload this bacth");

					}
				})
				.setBulkActions(1000)
				.setConcurrentRequests(10)
				.setFlushInterval(TimeValue.timeValueSeconds(5));

		return processor.build();
	}


}

