package com.springbatch_javatechie.reposritory;

import com.springbatch_javatechie.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepo extends JpaRepository<Customer,Integer> {


}
