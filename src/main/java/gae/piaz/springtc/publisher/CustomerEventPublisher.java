package gae.piaz.springtc.publisher;

import gae.piaz.springtc.controller.CustomerDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerEventPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishCustomerCreatedEvent(CustomerDTO data) {
        kafkaTemplate.send("customers", data);
        log.info("CustomerCreatedEvent sent to products topic");
    }
}