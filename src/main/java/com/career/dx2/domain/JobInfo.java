package com.career.dx2.domain;

import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

public class JobInfo {
    private JobDetail jobDetail;
    private Trigger trigger;
    private String identity;
    private String cronExpression;

    public static class Builder {
        private String cronExpression;
        private String identity;
        private JobDataMap jobDataMap = new JobDataMap();
        private Class<? extends Job> jobClass;

        public Builder cronExpression(String cronExpression) {
            this.cronExpression = cronExpression;
            return this;
        }

        public Builder newJob(Class<? extends Job> jobClass) {
            this.jobClass = jobClass;
            return this;
        }

        public Builder withIdentity(String identity) {
            this.identity = identity;
            return this;
        }

        public Builder addJobData(String key, Object value) {
            jobDataMap.put(key, value);
            return this;
        }

        public JobInfo build() throws Exception {
            if ("".equals(cronExpression)) {
                throw new IllegalArgumentException("necessary cronExpression field");
            }

            if ("".equals(identity)) {
                throw new IllegalArgumentException("necessary identity field");
            }

            if (jobClass == null) {
                throw new IllegalArgumentException("necessary jobClass field");
            }

            CronScheduleBuilder schedBuilder = CronScheduleBuilder.cronSchedule(new CronExpression(cronExpression));
            Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(identity)
                .withSchedule(schedBuilder)
                .build();

            JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .setJobData(jobDataMap)
                .build();

            JobInfo v = new JobInfo();
            v.jobDetail = jobDetail;
            v.trigger = trigger;
            v.cronExpression = cronExpression;
            v.identity = identity;
            return v;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public JobDetail getJobDetail() {
        return jobDetail;
    }

    public Trigger getTrigger() {
        return trigger;
    }

    public String getIdentity() {
        return identity;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    @Override
    public String toString() {
        return "JobInfo [cronExpression=" + cronExpression + ", identity=" + identity + ", jobDetail=" + jobDetail
                + ", trigger=" + trigger + "]";
    }
}
