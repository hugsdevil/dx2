package com.career.dx2.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Job 청구서 생성
public class JobBillingCreator implements Job {
    Logger logger = LoggerFactory.getLogger(JobBillingCreator.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
    }
}
