package gae.piaz.springtc.listener;

import gae.piaz.springtc.controller.CustomerData;
import gae.piaz.springtc.data.Customer;
import gae.piaz.springtc.data.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
class CustomerEventListener {

    private final CustomerRepository customerRepository;

    @KafkaListener(topics = "customers")
    public void handleCustomerCreatedEvent(CustomerData customerData) {
        log.info("Customer event received from customer topic");
        Customer customer = new Customer();
        customer.setId(customerData.id());
        customer.setName(customerData.name());
        customerRepository.save(customer);
    }

}