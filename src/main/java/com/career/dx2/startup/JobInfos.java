package com.career.dx2.startup;

import java.util.Arrays;
import java.util.List;

import com.career.dx2.domain.JobInfo;
import com.career.dx2.job.JobBillingCreator;

public class JobInfos {
    public static List<JobInfo> jobInfos() throws Exception {
        return Arrays.asList(
            createPrepaymentBill()                   // 선불 청구서 생성
        );
    }

    // 선불 청구서 생성
    public static JobInfo createPrepaymentBill() throws Exception {
        JobInfo jobInfo = JobInfo.builder()
            .cronExpression("0 0 20 * * ?")
            .newJob(JobBillingCreator.class)
            .withIdentity("createPrepaymentBill") // group도 있을 수 있지만 현재는 그냥 DEFAULT group
            .addJobData("prodDivCd", "PREPAY")
            .build();
        return jobInfo;
    }
}
