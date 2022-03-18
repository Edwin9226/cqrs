package com.banking.account.cmd.infrastructure;

import com.banking.cqrs.common.events.BaseEvent;
import com.banking.cqrs.common.producers.EventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class AccountEventProducer implements EventProducer {

    //inicializar un template kafka inyectar
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    /**
     * enviar mensaje hacia el topic
     * @param topic canal de comunicaciòn
     * @param event
     */
    @Override
    public void producer(String topic, BaseEvent event) {
        this.kafkaTemplate.send(topic, event);

    }
}
