package com.lunghwan.batch.guide.configuration;

import com.lunghwan.batch.guide.entity.Pay;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JdbcCursorItemReaderJobConfiguration {

    private static final int CHUNK_SIZE = 10;

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    private final DataSource dataSource;


    @Bean
    public Job jdbcCursorItemReaderJob(JobRepository jobRepository,
                                       Step jdbcCursorItemReaderStep) {
        return new JobBuilder("jdbcCursorItemReaderJob", jobRepository)
                .start(jdbcCursorItemReaderStep)
                .build();
    }

    @Bean
    public Step jdbcCursorItemReaderStep() {
        return new StepBuilder("jdbcCursorItemReaderStep", jobRepository)
                .<Pay, Pay>chunk(CHUNK_SIZE, transactionManager)
                .reader(jdbcCursorItemReader())
                .writer(jdbcCursorItemWriter())
                .build();
    }

    @Bean
    public JdbcCursorItemReader<Pay> jdbcCursorItemReader() {
        return new JdbcCursorItemReaderBuilder<Pay>()
                .name("jdbcCursorItemReader")
                .dataSource(dataSource)
                .fetchSize(CHUNK_SIZE)
                .rowMapper(new BeanPropertyRowMapper<>(Pay.class))
                .sql("SELECT id, amount, tx_name, tx_date_time FROM pay ORDER BY id")
                .build();
    }

    @Bean
    public ItemWriter<Pay> jdbcCursorItemWriter() {
        return items -> {
            for (Pay pay : items) {
                log.info("Current Pay={}", pay);
            }
        };
    }
}