package com.aws.elasticsearch.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.aws.elasticsearch.config.MethodExecutionTime;
import com.aws.elasticsearch.dto.SearchResponseDetails;
import com.aws.elasticsearch.entity.PlanDetails;
import com.aws.elasticsearch.service.IPlanService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@Controller
@RestController
public class PlanController {
	
	private static final Logger logger = LoggerFactory.getLogger(PlanController.class.getName());

	@Autowired
	private IPlanService service;

	@Autowired
	JobLauncher jobLauncher;

	@Autowired
	Job readDataFromFile;

	@ApiOperation(value = "Reading plan details", notes = "This Api is used to retrieve plan document by the given ID", response = String.class)
	@ApiResponses({ @ApiResponse(code = 200, message = "Successfully fetched the plan", response = String.class),
			@ApiResponse(code = 404, message = "Invalid identification number", response = PlanController.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = PlanController.class) })
	@RequestMapping(value = "/{plan-id}/plans", method = RequestMethod.GET, produces = "application/json")
	@MethodExecutionTime
	public ResponseEntity<Object> findById(
			@ApiParam(value = "profile-id") @RequestParam(name = "planID", required = true) String planId)
			throws Exception {

		PlanDetails planInfo = null;
		ResponseEntity<Object> response = null;

		try {
			planInfo = service.getPlanDetails(planId);

			if (planInfo != null && !StringUtils.isEmpty(planInfo)) {
				response = new ResponseEntity<Object>(planInfo, HttpStatus.OK);
			} else {
				response = new ResponseEntity<>("No data found for given input details", HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			response = new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return response;

	}

	@ApiOperation(value = "Search Plan Details", notes = "This Api is to used  search plan deatils base don the given search criteria", response = String.class)
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successfully fetched  all the matched plan details", response = String.class),
			@ApiResponse(code = 404, message = "Plan details not found", response = PlanController.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = PlanController.class) })
	@RequestMapping(value = "/plans", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	@MethodExecutionTime
	public ResponseEntity<Object> searchPlanDetails(
			@ApiParam(value = "Search By", allowableValues = "PLAN_NAME,SPONSOR_NAME,SPONSOR_STATE") @RequestParam(name = "searchBy", required = true) String searchBy,
			@ApiParam(value = "queryText") @RequestParam(name = "text", required = true) String text) {

		ResponseEntity<Object> responseEntity = null;
		SearchResponseDetails responseDetails = null;
		try {

			responseDetails = service.searchPlanDetails(searchBy, text);
			if (responseDetails != null && responseDetails.getPlans().size() !=0) {
				responseEntity = new ResponseEntity<Object>(responseDetails, HttpStatus.OK);
			} else {
				responseEntity = new ResponseEntity<>("No data found for given input details", HttpStatus.BAD_REQUEST);
			}
			
		} catch (Exception e) {
			responseEntity = new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return responseEntity;
	}

	@ApiOperation(value = "Upload plan details from csv file", notes = "This Api is to upload plan deatils", response = String.class)
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successfully uploaded plandetails content", response = Boolean.class),
			@ApiResponse(code = 500, message = "Internal Error", response = PlanController.class) })
	@RequestMapping(value = "/plans/upload", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	@MethodExecutionTime
	public ResponseEntity<Object> uploadPlansData(@ApiParam(value = "queryText") @RequestParam(name = "File Path", required = true) String filePath) {

		ResponseEntity<Object> responseEntity = null;

		try {

           
			JobParameters jobParameters =  new JobParametersBuilder().addParameter("filePath",
					new JobParameter(filePath)).toJobParameters();
			
			JobExecution execution = jobLauncher.run(readDataFromFile, jobParameters);
			logger.info("Exit Status : " + execution.getStatus());
			responseEntity = new ResponseEntity<>("Successfully uploaded plans information", HttpStatus.OK);
		} catch (Exception ex) {
			responseEntity = new ResponseEntity<Object>("Problem occured while saving file",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}

}