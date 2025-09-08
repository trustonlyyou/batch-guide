package com.lunghwan.batch.guide.tasklet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
//@StepScope
public class SimpleTasklet implements Tasklet {

    @Value("#{jobParameters['requestDate']}")
    String requestDate;

    public SimpleTasklet() {
        log.info(">>>>> Create Tasklet");
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info(">>>>> This is scopeStep1");
        log.info(">>>>> requestDate = {}", requestDate);
        return RepeatStatus.FINISHED;
    }
}
