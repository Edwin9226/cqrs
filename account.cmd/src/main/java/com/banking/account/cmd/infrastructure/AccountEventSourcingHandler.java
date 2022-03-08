package com.banking.account.cmd.infrastructure;

import com.banking.account.cmd.domain.AccountAggregate;
import com.banking.cqrs.common.domains.AgregateRoot;
import com.banking.cqrs.common.handlers.EventSourcingHandlers;
import com.banking.cqrs.common.infrastructure.EventStore;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
public class AccountEventSourcingHandler implements EventSourcingHandlers<AccountAggregate> {
    @Autowired
    private EventStore eventStore;

    @Override
    public void save(AgregateRoot aggregate) {
        eventStore.saveEvents(aggregate.getId(), aggregate.getUncommitedChange(), aggregate.getVersion());
        //marca que los cambios han sido enviados exitosamente
        aggregate.markChangesAsCommited();
    }

    @Override
    public AccountAggregate getById(String id) {
        val aggregate = new AccountAggregate();
        val events = eventStore.getEvent(id);
        if(events !=null && events.isEmpty()){
            aggregate.replaceEvents(events);
            val latestVersion= events.stream().map(x->x.getVersion()).max(Comparator.naturalOrder());
            aggregate.setVersion(latestVersion.get());
        }
        return  aggregate;
    }
}
