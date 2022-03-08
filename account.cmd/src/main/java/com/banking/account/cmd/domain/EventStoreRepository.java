package com.banking.account.cmd.domain;

import com.banking.cqrs.common.events.EventModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EventStoreRepository extends MongoRepository<EventModel,String> {
    /**
     * lista de eventos busca por el agregator identificador.
     * @param aggregateIdentifier
     * @return
     */
    List<EventModel> findByAggregateIdentifier(String aggregateIdentifier);

}
