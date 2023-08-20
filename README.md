# Dx Batch 프로젝트

## Quartz 스케쥴러
1. pom.xml 설정
1. quartz bean 주입 설정
1. job 설정

pom.xml
```xml
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-quartz</artifactId>
		</dependency>
```

quartz bean 주입 설정
```java
public final class AutowiringSpringBeanJobFactory extends SpringBeanJobFactory {

    private transient AutowireCapableBeanFactory beanFactory;

    @Override
    public void setApplicationContext(final ApplicationContext context) {
        beanFactory = context.getAutowireCapableBeanFactory();
    }

    @Override
    protected Object createJobInstance(final TriggerFiredBundle bundle) throws Exception {
        final Object job = super.createJobInstance(bundle);
        beanFactory.autowireBean(job);
        return job;
    }
}
```

job 설정
1. job 생성
1. job 실행

job 생성
```java
public class JobBillingCreator implements Job {
    Logger logger = LoggerFactory.getLogger(JobBillingCreator.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
    }
}
```

job 실행
```java
public class JobInfos {
    public static List<JobInfo> jobInfos() throws Exception {
        return Arrays.asList(
            createPrepaymentBill()                   // 선불 청구서 생성
        );
    }

    // 선불 청구서 생성
    public static JobInfo createPrepaymentBill() throws Exception {
        JobInfo jobInfo = JobInfo.builder()
            .cronExpression("0 0 20 * * ?")       // 매일 20시 마다
            .newJob(JobBillingCreator.class)
            .withIdentity("createPrepaymentBill") // group도 있을 수 있지만 현재는 그냥 DEFAULT group
            .addJobData("prodDivCd", "PREPAY")
            .build();
        return jobInfo;
    }
}
```

```java
@Configuration
public class ApplicationStartup implements ApplicationRunner {
    Logger logger = LoggerFactory.getLogger(ApplicationStartup.class);

    @Autowired
    SchedulerFactoryBean factoryBean;

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
```