package com.career.dx2.startup;

import java.util.Arrays;

import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.career.dx2.domain.JobInfo;

@Configuration
public class ApplicationStartup implements ApplicationRunner {
    Logger logger = LoggerFactory.getLogger(ApplicationStartup.class);

    @Autowired
    SchedulerFactoryBean factoryBean;

	@Bean
	CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			logger.info("Let's inspect the beans provided by Spring Boot:");
			String[] beanNames = ctx.getBeanDefinitionNames();
			Arrays.sort(beanNames);
			for (String beanName : beanNames) {
				logger.info(beanName);
			}
		};
	}

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Scheduler scheduler = factoryBean.getScheduler();
        scheduler.start();

        for (JobInfo jobInfo : JobInfos.jobInfos()) {
            logger.info("start job: {} {}", jobInfo.getIdentity(), jobInfo.getCronExpression());
            scheduler.scheduleJob(jobInfo.getJobDetail(), jobInfo.getTrigger());
        }
    }
    
}
