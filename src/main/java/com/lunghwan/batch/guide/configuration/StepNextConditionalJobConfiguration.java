package com.lunghwan.batch.guide.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
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
public class StepNextConditionalJobConfiguration {


    // Job 정의
    @Bean
    public Job stepNextConditionalJob(JobRepository jobRepository,
                                      Step conditionalJobStep1, Step conditionalJobStep2, Step conditionalJobStep3) {
        return new JobBuilder("stepNextConditionalJob", jobRepository)
                .start(conditionalJobStep1)
                    .on("FAILED")       // FAILED 일 경우
                    .to(conditionalJobStep3)    // step3으로 이동한다.
                    .on("*")            // step3의 결과 관계 없이
                    .end()                      // step3으로 이동하면 Flow가 종료한다.
                .from(conditionalJobStep1)      // step1로부터
                    .on("*")            // FAILED 외에 모든 경우
                    .to(conditionalJobStep2)    // step2로 이동한다.
                    .next(conditionalJobStep3)  // step2가 정상 종료되면 step3으로 이동한다.
                    .on("*")            // step3의 결과 관계 없이
                    .end()                      // step3으로 이동하면 Flow가 종료한다.
                .end()                          // Job 종료
                .build();
    }

    // Step1 정의
    @Bean
    public Step conditionalJobStep1(JobRepository jobRepository,
                                    PlatformTransactionManager transactionManager) {
        return new StepBuilder("step1", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    log.info("==================================================");
                    log.info(">>>>> This is stepNextConditionalJob Step1");
                    /**
                     * ExitStatus를 FAILED로 지정한다.
                     * 해당 status를 보고 flow가 진행된다.
                     */
                    // contribution.setExitStatus(ExitStatus.FAILED);
                    return RepeatStatus.FINISHED;
                }), transactionManager)
                .build();
    }

    // Step2 정의
    @Bean
    public Step conditionalJobStep2(JobRepository jobRepository,
                                    PlatformTransactionManager transactionManager) {
        return new StepBuilder("step2", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    log.info("==================================================");
                    log.info(">>>>> This is stepNextConditionalJob Step2");
                    return RepeatStatus.FINISHED;
                }), transactionManager)
                .build();
    }

    // Step3 정의
    @Bean
    public Step conditionalJobStep3(JobRepository jobRepository,
                                    PlatformTransactionManager transactionManager) {
        return new StepBuilder("step3", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    log.info("==================================================");
                    log.info(">>>>> This is stepNextConditionalJob Step3");
                    return RepeatStatus.FINISHED;
                }), transactionManager)
                .build();
    }
}
