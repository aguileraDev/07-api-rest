package com.training.restaurant.services.interfaces;

import com.training.restaurant.dto.CreateCustomerDto;
import com.training.restaurant.dto.UpdateCustomerDto;
import com.training.restaurant.models.Customer;

import java.util.List;

public interface ICustomerService {
    Customer createCustomer(CreateCustomerDto createCustomerDto);
    List<Customer> findAllCustomers();
    Customer findCustomerById(Long id);
    void updateCustomer(Long id, UpdateCustomerDto updateCustomerDto);
    void removeCustomer(Long id);
}
