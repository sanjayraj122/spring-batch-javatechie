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

        System.out.println("Writer thread name: " + Thread.currentThread().getName());
           customerRepo.saveAll(list);
       }
    }

