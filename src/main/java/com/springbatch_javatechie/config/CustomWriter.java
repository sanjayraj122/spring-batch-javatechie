package com.springbatch_javatechie.config;

import com.springbatch_javatechie.entity.Customer;
import com.springbatch_javatechie.reposritory.CustomerRepo;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomWriter implements ItemWriter<Customer> {

    @Autowired
    private CustomerRepo customerRepo;
    @Override
    public void write(List list) throws Exception {

        if (list != null && !list.isEmpty()) {
            System.out.println("thread name: " + Thread.currentThread().getName() + " writing data to database");
            customerRepo.saveAll(list);
        } else {
            System.out.println("No data to write.");
        }
    }
}
