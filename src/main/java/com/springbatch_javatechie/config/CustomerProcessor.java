package com.springbatch_javatechie.config;

import com.springbatch_javatechie.entity.Customer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class CustomerProcessor implements ItemProcessor<Customer, Customer> {
    @Override
    public Customer process(Customer customer) throws Exception {
//        if (customer.getCountry().equals("United States")) {
//            return customer;
//        } else {
//            return null;
//        }


        return customer;
    }
}
