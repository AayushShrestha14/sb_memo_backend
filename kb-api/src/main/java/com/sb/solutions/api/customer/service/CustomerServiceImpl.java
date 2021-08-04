package com.sb.solutions.api.customer.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import lombok.AllArgsConstructor;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.sb.solutions.api.customer.entity.Customer;
import com.sb.solutions.api.customer.repository.CustomerRepository;
import com.sb.solutions.api.customer.repository.specification.CustomerSpecBuilder;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository customerRepository;

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer findOne(Long id) {
        return customerRepository.getOne(id);
    }

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Page<Customer> findAllPageable(Object t, Pageable pageable) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> s = objectMapper.convertValue(t, Map.class);
        s.values().removeIf(Objects::isNull);
        final CustomerSpecBuilder customerSpecBuilder = new CustomerSpecBuilder(s);
        Specification<Customer> specification = customerSpecBuilder.build();
        return customerRepository.findAll(specification, pageable);
    }

    @Override
    public List<Customer> saveAll(List<Customer> list) {
        return customerRepository.saveAll(list);
    }

    @Override
    public Customer findCustomerByCitizenshipNumber(String citizenshipNumber) {
        return customerRepository.findCustomerByCitizenshipNumber(citizenshipNumber);
    }
}

