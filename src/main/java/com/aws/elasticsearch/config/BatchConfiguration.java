package com.aws.elasticsearch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import com.aws.elasticsearch.entity.PlanDetails;
import com.aws.elasticsearch.utility.ElasticSearchWriter;

@Configuration
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@Primary
public class BatchConfiguration extends DefaultBatchConfigurer{


	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	ElasticSearchWriter elasticSearchwriter;
	
	 private static final String OVERRIDDEN_BY_EXPRESSION = null;
	

	@Bean
	public Job uploadDataToES() {
		return jobBuilderFactory
				.get("uploadDataToES")
				.incrementer(new RunIdIncrementer())
				.start(uploadStep())
				.build();
	}

	@Bean
	public TaskExecutor taskExecutor(){
		SimpleAsyncTaskExecutor asyncTaskExecutor=new SimpleAsyncTaskExecutor("spring_batch");
		asyncTaskExecutor.setConcurrencyLimit(5);
		return asyncTaskExecutor;
	}


	@Bean
	public Step uploadStep() {
		return stepBuilderFactory.get("uploadStep").<Object, PlanDetails>chunk(1000)
				.reader(csvReader(OVERRIDDEN_BY_EXPRESSION))
				.writer(elasticSearchwriter)
				.taskExecutor(taskExecutor())
				.build();
	}

	@Bean
	@StepScope
	public FlatFileItemReader<PlanDetails> csvReader(@Value("#{jobParameters[filePath]}") String filePath) {
		FlatFileItemReader<PlanDetails> reader = new FlatFileItemReader<>();
		
		reader.setResource(new FileSystemResource(filePath));
		reader.setLinesToSkip(1);  

		reader.setLineMapper(new DefaultLineMapper<PlanDetails>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames(new String[] { "ACK_ID",
								"FORM_PLAN_YEAR_BEGIN_DATE",
								"FORM_TAX_PRD",
								"TYPE_PLAN_ENTITY_CD",
								"TYPE_DFE_PLAN_ENTITY_CD",
								"INITIAL_FILING_IND",
								"AMENDED_IND",
								"FINAL_FILING_IND",
								"SHORT_PLAN_YR_IND",
								"COLLECTIVE_BARGAIN_IND",
								"F5558_APPLICATION_FILED_IND",
								"EXT_AUTOMATIC_IND",
								"DFVC_PROGRAM_IND",
								"EXT_SPECIAL_IND",
								"EXT_SPECIAL_TEXT",
								"PLAN_NAME",
								"SPONS_DFE_PN",
								"PLAN_EFF_DATE",
								"SPONSOR_DFE_NAME",
								"SPONS_DFE_DBA_NAME",
								"SPONS_DFE_CARE_OF_NAME",
								"SPONS_DFE_MAIL_US_ADDRESS1",
								"SPONS_DFE_MAIL_US_ADDRESS2",
								"SPONS_DFE_MAIL_US_CITY",
								"SPONS_DFE_MAIL_US_STATE",
								"SPONS_DFE_MAIL_US_ZIP",
								"SPONS_DFE_MAIL_FOREIGN_ADDR1",
								"SPONS_DFE_MAIL_FOREIGN_ADDR2",
								"SPONS_DFE_MAIL_FOREIGN_CITY",
								"SPONS_DFE_MAIL_FORGN_PROV_ST",
								"SPONS_DFE_MAIL_FOREIGN_CNTRY",
								"SPONS_DFE_MAIL_FORGN_POSTAL_CD",
								"SPONS_DFE_LOC_US_ADDRESS1",
								"SPONS_DFE_LOC_US_ADDRESS2",
								"SPONS_DFE_LOC_US_CITY",
								"SPONS_DFE_LOC_US_STATE",
								"SPONS_DFE_LOC_US_ZIP",
								"SPONS_DFE_LOC_FOREIGN_ADDRESS1",
								"SPONS_DFE_LOC_FOREIGN_ADDRESS2",
								"SPONS_DFE_LOC_FOREIGN_CITY",
								"SPONS_DFE_LOC_FORGN_PROV_ST",
								"SPONS_DFE_LOC_FOREIGN_CNTRY",
								"SPONS_DFE_LOC_FORGN_POSTAL_CD",
								"SPONS_DFE_EIN",
								"SPONS_DFE_PHONE_NUM",
								"BUSINESS_CODE",
								"ADMIN_NAME",
								"ADMIN_CARE_OF_NAME",
								"ADMIN_US_ADDRESS1",
								"ADMIN_US_ADDRESS2",
								"ADMIN_US_CITY",
								"ADMIN_US_STATE",
								"ADMIN_US_ZIP",
								"ADMIN_FOREIGN_ADDRESS1",
								"ADMIN_FOREIGN_ADDRESS2",
								"ADMIN_FOREIGN_CITY",
								"ADMIN_FOREIGN_PROV_STATE",
								"ADMIN_FOREIGN_CNTRY",
								"ADMIN_FOREIGN_POSTAL_CD",
								"ADMIN_EIN",
								"ADMIN_PHONE_NUM",
								"LAST_RPT_SPONS_NAME",
								"LAST_RPT_SPONS_EIN",
								"LAST_RPT_PLAN_NUM",
								"ADMIN_SIGNED_DATE",
								"ADMIN_SIGNED_NAME",
								"SPONS_SIGNED_DATE",
								"SPONS_SIGNED_NAME",
								"DFE_SIGNED_DATE",
								"DFE_SIGNED_NAME",
								"TOT_PARTCP_BOY_CNT",
								"TOT_ACTIVE_PARTCP_CNT",
								"RTD_SEP_PARTCP_RCVG_CNT",
								"RTD_SEP_PARTCP_FUT_CNT",
								"SUBTL_ACT_RTD_SEP_CNT",
								"BENEF_RCVG_BNFT_CNT",
								"TOT_ACT_RTD_SEP_BENEF_CNT",
								"PARTCP_ACCOUNT_BAL_CNT",
								"SEP_PARTCP_PARTL_VSTD_CNT",
								"CONTRIB_EMPLRS_CNT",
								"TYPE_PENSION_BNFT_CODE",
								"TYPE_WELFARE_BNFT_CODE",
								"FUNDING_INSURANCE_IND",
								"FUNDING_SEC412_IND",
								"FUNDING_TRUST_IND",
								"FUNDING_GEN_ASSET_IND",
								"BENEFIT_INSURANCE_IND",
								"BENEFIT_SEC412_IND",
								"BENEFIT_TRUST_IND",
								"BENEFIT_GEN_ASSET_IND",
								"SCH_R_ATTACHED_IND",
								"SCH_MB_ATTACHED_IND",
								"SCH_SB_ATTACHED_IND",
								"SCH_H_ATTACHED_IND",
								"SCH_I_ATTACHED_IND",
								"SCH_A_ATTACHED_IND",
								"NUM_SCH_A_ATTACHED_CNT",
								"SCH_C_ATTACHED_IND",
								"SCH_D_ATTACHED_IND",
								"SCH_G_ATTACHED_IND",
								"FILING_STATUS",
								"DATE_RECEIVED",
								"VALID_ADMIN_SIGNATURE",
								"VALID_DFE_SIGNATURE",
								"VALID_SPONSOR_SIGNATURE",
								"ADMIN_PHONE_NUM_FOREIGN",
								"SPONS_DFE_PHONE_NUM_FOREIGN",
								"ADMIN_NAME_SAME_SPON_IND",
								"ADMIN_ADDRESS_SAME_SPON_IND",
								"PREPARER_NAME",
								"PREPARER_FIRM_NAME",
								"PREPARER_US_ADDRESS1",
								"PREPARER_US_ADDRESS2",
								"PREPARER_US_CITY",
								"PREPARER_US_STATE",
								"PREPARER_US_ZIP",
								"PREPARER_FOREIGN_ADDRESS1",
								"PREPARER_FOREIGN_ADDRESS2",
								"PREPARER_FOREIGN_CITY",
								"PREPARER_FOREIGN_PROV_STATE",
								"PREPARER_FOREIGN_CNTRY",
								"PREPARER_FOREIGN_POSTAL_CD",
								"PREPARER_PHONE_NUM",
								"PREPARER_PHONE_NUM_FOREIGN",
								"TOT_ACT_PARTCP_BOY_CNT",
								"SUBJ_M1_FILING_REQ_IND",
								"COMPLIANCE_M1_FILING_REQ_IND",
								"M1_RECEIPT_CONFIRMATION_CODE",
								"ADMIN_MANUAL_SIGNED_DATE",
								"ADMIN_MANUAL_SIGNED_NAME",
								"LAST_RPT_PLAN_NAME",
								"SPONS_MANUAL_SIGNED_DATE",
								"SPONS_MANUAL_SIGNED_NAME",
								"DFE_MANUAL_SIGNED_DATE",
								"DFE_MANUAL_SIGNED_NAME"



						});
					}
				});
				setFieldSetMapper(new BeanWrapperFieldSetMapper<PlanDetails>() {
					{
						setTargetType(PlanDetails.class);
					}
				});
			}
		});
		return reader;
	}




}



