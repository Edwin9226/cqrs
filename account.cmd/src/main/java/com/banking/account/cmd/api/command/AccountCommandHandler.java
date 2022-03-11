package com.banking.account.cmd.api.command;

import com.banking.account.cmd.domain.AccountAggregate;
import com.banking.cqrs.common.handlers.EventSourcingHandlers;
import lombok.val;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountCommandHandler implements CommandHandler{
    /**
     * eevntsSourcingHandler para poder guardar el ultimo estado de un agregate
     * en cada uno de estos mètodos.
     */
    @Autowired
    private EventSourcingHandlers<AccountAggregate> eventSourcingHandlers;

    @Override
    public void handle(OpenAccountCommand command) {
        var aggregate = new AccountAggregate(command);
        eventSourcingHandlers.save(aggregate);
    }

    @Override
    public void handle(DepositFundsCommand command) {
        var aggregate= eventSourcingHandlers.getById(command.getId());
        aggregate.depositFunds(command.getAmount());
        eventSourcingHandlers.save(aggregate);
    }

    @Override
    public void handle(WithdrawFundsCommand command) {
        var aggregate= eventSourcingHandlers.getById(command.getId());
        if(command.getAmount()>aggregate.getBalance()){
            throw  new IllegalStateException("Insuficientes Fondos");

        }
        aggregate.withdrawFunds(command.getAmount());
        eventSourcingHandlers.save(aggregate);
    }

    @Override
    public void handle(CloseAccountCommand command) {
        var aggregate= eventSourcingHandlers.getById(command.getId());
         aggregate.closeAccount();
         eventSourcingHandlers.save(aggregate);
    }
}
