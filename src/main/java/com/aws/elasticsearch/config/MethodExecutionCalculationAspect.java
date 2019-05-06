package com.aws.elasticsearch.config;

import java.util.Calendar;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MethodExecutionCalculationAspect {

	private static final Logger logger = LoggerFactory.getLogger(MethodExecutionCalculationAspect.class);

	@Around("@annotation(MethodExecutionTime)")
	public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
		long startTime = Calendar.getInstance().getTimeInMillis();

		Object proceed = joinPoint.proceed();

		long executionTime = Calendar.getInstance().getTimeInMillis() - startTime;

		logger.info(joinPoint.getSignature() + " executed in {} sec", executionTime / 1000.0);

		return proceed;
	}
}
