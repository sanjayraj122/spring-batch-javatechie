package com.springbatch_javatechie.config;


import com.springbatch_javatechie.entity.Customer;
import com.springbatch_javatechie.listener.SkipStepListener;
import com.springbatch_javatechie.partition.ColumnRangePartitioner;
import com.springbatch_javatechie.reposritory.CustomerRepo;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableBatchProcessing
@Configuration
@AllArgsConstructor
public class SpringBatchConfig {

    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;
    private CustomWriter customerWriter;
    private CustomerRepo customerRepository;

    @Bean
    public FlatFileItemReader<Customer> reader() {
        FlatFileItemReader itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("src/main/resources/customers.csv"));
        itemReader.setName("csvReader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());

        return itemReader;
    }

    @Bean
    public LineMapper<Customer> lineMapper() {
        DefaultLineMapper<Customer> lineMapper = new DefaultLineMapper<>();

        // lineTokenizer is used extract the value from csv file
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("id", "firstName", "lastName", "email", "gender", "contactNo", "country", "dob", "age");

        //fieldSetMapper is used to map the mapped values to the Customer class
        BeanWrapperFieldSetMapper<Customer> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Customer.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }

    @Bean
    public CustomerProcessor processor() {
        return new CustomerProcessor();
    }

    @Bean
    public RepositoryItemWriter<Customer> writer() {
        RepositoryItemWriter<Customer> writer = new RepositoryItemWriter<>();
        writer.setRepository(customerRepository);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("slaveStep")
                .<Customer, Customer>chunk(5)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .faultTolerant()
//                .skipLimit(100)
//                .skip(NumberFormatException.class)
                .listener(skipListener())
                .skipPolicy(skipPolicy())
                .build();
    }

    @Bean
    public SkipPolicy skipPolicy() {
        return new ExceptionSkipPolicy();
    }

    @Bean
    public SkipListener skipListener() {
        return new SkipStepListener();
    }

//    @Bean
//    public Step slaveStep() {
//        return stepBuilderFactory.get("slaveStep").<Customer, Customer>chunk(500)
//                .reader(reader())
//                .processor(processor())
//                .writer(customerWriter)
//
//                .build();
//    }
//
//    @Bean
//    public Step masterStep() {
//        return stepBuilderFactory.get("masterStep").
//                partitioner(slaveStep().getName(), partitioner())
//                .partitionHandler(partitionHandler())
//                .build();
//    }

    @Bean
    public Job job() {
        return jobBuilderFactory.get("importCustomer")
                .flow(step1())
                .end()
                .build();
    }

//    @Bean
//    public TaskExecutor taskExecutor() {
//        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
//        taskExecutor.setCorePoolSize(4);
//        taskExecutor.setMaxPoolSize(4);
//        taskExecutor.setQueueCapacity(4);
//        return taskExecutor;
//    }

//    @Bean
//    public ColumnRangePartitioner partitioner() {
//        return new ColumnRangePartitioner();
//    }

//    @Bean
//    public PartitionHandler partitionHandler() {
//        TaskExecutorPartitionHandler partitionHandler = new TaskExecutorPartitionHandler();
//        partitionHandler.setGridSize(4);
//        partitionHandler.setTaskExecutor(taskExecutor());
//        partitionHandler.setStep(slaveStep());
//        return partitionHandler;
//    }


}
