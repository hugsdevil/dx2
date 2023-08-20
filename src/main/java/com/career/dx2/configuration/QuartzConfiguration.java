package com.career.dx2.configuration;

import javax.sql.DataSource;

import org.quartz.spi.JobFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.career.dx2.util.AutowiringSpringBeanJobFactory;

@Configuration
public class QuartzConfiguration {
    @Bean
    JobFactory jobFactory(ApplicationContext ctx) {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(ctx);
        return jobFactory;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource, JobFactory jobFactory) throws Exception {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        // this allows to update triggers in DB when updating settings in config file:
        factory.setJobFactory(jobFactory);
        factory.afterPropertiesSet();
        return factory;
    }
}
