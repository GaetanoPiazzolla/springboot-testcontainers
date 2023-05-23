package gae.piaz.springtc.service;

import gae.piaz.springtc.controller.CustomerData;
import gae.piaz.springtc.data.Customer;
import gae.piaz.springtc.data.CustomerRepository;
import gae.piaz.springtc.publisher.CustomerEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

import static gae.piaz.springtc.config.RedisConfig.CUSTOMER_CACHE;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerEventPublisher publisher;

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Cacheable(CUSTOMER_CACHE)
    public List<Customer> findByName(String name) {
        log.info("------ Hitting database & not using cache! ------ ");
        return customerRepository.findByNameIgnoreCase(name);
    }

    public void saveAsync(CustomerData data) {
        publisher.publishCustomerCreatedEvent(data);
    }
}
