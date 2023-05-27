package gae.piaz.springtc.listener;

import gae.piaz.springtc.controller.CustomerDTO;
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
    public void handleCustomerCreatedEvent(CustomerDTO customerDTO) {
        log.info("Customer event received from customer topic");
        Customer customer = new Customer();
        customer.setId(customerDTO.id());
        customer.setName(customerDTO.name());
        customerRepository.save(customer);
    }

}