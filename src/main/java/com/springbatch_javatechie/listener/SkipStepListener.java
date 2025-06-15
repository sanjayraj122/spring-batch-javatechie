package com.springbatch_javatechie.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springbatch_javatechie.entity.Customer;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.StepExecution;

public class SkipStepListener implements SkipListener<Customer, Number> {

    Logger logger = LoggerFactory.getLogger(SkipStepListener.class);

    //    StepExecution stepExecution= new StepExecution();
    @Override
    public void onSkipInRead(Throwable throwable) {

        logger.info("Skip in Read {}", throwable.getMessage());
    }

    @Override
    public void onSkipInWrite(Number item, Throwable throwable) {

        logger.info("Skip in write {}", throwable.getMessage(), item);
    }

    @SneakyThrows
    @Override
    public void onSkipInProcess(Customer customer, Throwable throwable) {
        logger.info("Skip in process {}, {}", customer, throwable.getMessage());
        logger.info("Item {}  skipped due to error  {}" , new ObjectMapper().writeValueAsString(customer) , throwable.getMessage());
    }
}
