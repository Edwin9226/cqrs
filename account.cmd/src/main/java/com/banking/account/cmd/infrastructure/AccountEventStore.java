package com.banking.account.cmd.infrastructure;

import com.banking.account.cmd.domain.AccountAggregate;
import com.banking.account.cmd.domain.EventStoreRepository;
import com.banking.cqrs.common.events.BaseEvent;
import com.banking.cqrs.common.events.EventModel;
import com.banking.cqrs.common.exceptions.AggregateNotFoundException;
import com.banking.cqrs.common.exceptions.ConcurrencyException;
import com.banking.cqrs.common.infrastructure.EventStore;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountEventStore implements EventStore {
    /**
     * Implementacion del repository
     */
    @Autowired
    private EventStoreRepository eventStoreRepository;

    @Override
    public void saveEvents(String aggregateId, Iterable<BaseEvent> events, int expectedVersion) {
     val eventStream = eventStoreRepository.findByAggregateIdentifier(aggregateId);
     if(expectedVersion != -1 && eventStream.get(eventStream.size()-1).getVersion() != expectedVersion){
        throw new ConcurrencyException();
     }

     int version = expectedVersion;
     for( val event:events){
          version++;
          event.setVersion(version);
          val eventModel= EventModel.builder()
                  .timeStamp(new Date())
                  .aggregateIdentifier(aggregateId)
                  .aggregateType(AccountAggregate.class.getTypeName())
                  .version(version)
                  .eventType(event.getClass().getTypeName())
                  .eventData(event)
                  .build();
          val persistedEvent = eventStoreRepository.save(eventModel);
          if(persistedEvent != null){
              // debo llamar al kafka, producir un evento para kafka
          }
     }
    }

    @Override
    public List<BaseEvent> getEvent(String aggregateId) {
        val eventStream = eventStoreRepository.findByAggregateIdentifier(aggregateId);
       if(eventStream == null || eventStream.isEmpty()){
           throw new AggregateNotFoundException("La cuenta del Banco es incorrecta");
       }
       return eventStream.stream().map(x->x.getEventData()).collect(Collectors.toList());
    }
}
