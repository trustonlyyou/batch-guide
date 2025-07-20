package com.lunghwan.batch.guide.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Slf4j
public class StepNextJobConfiguration {

    // Job 정의
    @Bean
    public Job stepNextJob(JobRepository jobRepository,
                           Step step1,
                           Step step2,
                           Step step3) {
        return new JobBuilder("stepNextJob", jobRepository)
                .start(step1)
                .next(step2)
                .next(step3)
                .build();
    }

    // Step1 정의
    @Bean
    public Step step1(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager) {
        return new StepBuilder("stpe1", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    log.info(">>>>> This is Step1");
                    return RepeatStatus.FINISHED;
                }), transactionManager)
                .build();

    }

    // Step2 정의
    @Bean
    public Step step2(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager) {
        return new StepBuilder("stpe2", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    log.info(">>>>> This is Step1");
                    return RepeatStatus.FINISHED;
                }), transactionManager)
                .build();

    }

    // Step3 정의
    @Bean
    public Step step3(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager) {
        return new StepBuilder("stpe3", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    log.info(">>>>> This is Step1");
                    return RepeatStatus.FINISHED;
                }), transactionManager)
                .build();
    }
}
