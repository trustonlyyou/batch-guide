package com.lunghwan.batch.guide.configuration;

import com.lunghwan.batch.guide.tasklet.SimpleTasklet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class SimpleJobConfiguration {

    private final SimpleTasklet simpleTasklet;

    @Bean
    public Job simpleJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("simpleTaskletJob", jobRepository)
                .start(simpleTaskletStep1(jobRepository, transactionManager))
                .build();
    }

    public Step simpleTaskletStep1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        log.info(">>>>> This is simpleStep1");
        return new StepBuilder("simpleTaskletStep1", jobRepository)
                .tasklet(simpleTasklet, transactionManager)
                .build();
    }
}
