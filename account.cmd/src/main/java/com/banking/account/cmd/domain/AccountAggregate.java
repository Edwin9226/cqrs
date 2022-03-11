package com.banking.account.cmd.domain;

import com.banking.account.cmd.api.command.OpenAccountCommand;
import com.banking.account.common.events.AccountClosedEvent;
import com.banking.account.common.events.AccountOpenedEvent;
import com.banking.account.common.events.FundsDepositedEvent;
import com.banking.account.common.events.FundsWithdrawnEvent;
import com.banking.cqrs.common.domains.AgregateRoot;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
public class AccountAggregate extends AgregateRoot {
    private Boolean active;
    private double balance;

    /**
     * creacion de una cuenta bancaria.
     * contructor cuando llame al agregar, creara un nuevo evento
     * @param command data del cliente del command.
     */
    public AccountAggregate(OpenAccountCommand command){
     raiseEvent(AccountOpenedEvent.builder()
                     .id(command.getId())
                     .accountHolder(command.getAccountHolder())
                     .createDate(new Date())
                     .accountType(command.getAccountType())
                     .openingBalance(command.getOpeningBalance())
                     .build());

    }
    public void apply(AccountOpenedEvent event){
    this.id= event.getId();
    this.active= true;
    this.balance= event.getOpeningBalance();
    }


    /**
     * deposito en la cuenta
     * Valida si es activo y mayor a cero
     * crear o vuelve a ejecutar el event si se agrego
     * @param amount
     */
    public void depositFunds(double amount){
        if(!this.active){
            throw new IllegalStateException("Los fondos no pueden " +
                    "ser depositados en esta cuenta");
        }

        if(amount <=0){
            throw  new IllegalStateException("El deposito del dinero no puede ser sero o " +
                    "menor que cero.");
        }

        raiseEvent(FundsDepositedEvent.builder()
                .id(this.id)
                .amount(amount)
                .build());
    }

    public void apply(FundsDepositedEvent event){
        this.id= event.getId();
        this.balance += event.getAmount();
    }

    /**
     * retirar fondos de una cuenta.
     * valida si es activa y ejecuta el event y metodo apply
     * @param amount
     */
    public void  withdrawFunds(double amount){
        if(!active){
            throw new IllegalStateException("La cuenta bancaria esta cerrada");
        }

        raiseEvent(FundsWithdrawnEvent.builder()
                .id(this.id)
                .amount(amount)
                .build());
    }

    public void  apply (FundsWithdrawnEvent event){
        this.id= event.getId();
        this.balance -= event.getAmount();
    }

    /**
     * Cerrar una cuenta bancaria
     */
    public void closeAccount(){
        if(!active){
            throw new IllegalStateException("La cuenta de banco esta cerrada.");
        }
        raiseEvent(AccountClosedEvent.builder()
                .id(this.id)
                .build());
    }

    public void apply(AccountClosedEvent event){
        this.id= event.getId();
        this.active= false;
    }
}
