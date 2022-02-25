package com.banking.account.cmd.infrastructure;

import com.banking.cqrs.common.commands.BaseCommand;
import com.banking.cqrs.common.commands.CommandHandlerMethod;
import com.banking.cqrs.common.infrastructure.CommandDispatcher;
import lombok.experimental.var;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class AccountCommandDispatcher implements CommandDispatcher {

    /**
     * crear una variable que representa la colecciòn de los mètodos que estan
     * siendo registrados
     *  key indice que quiero que almacene sea una class.
     *  List que represente los commandHandlerMethod
     */
    private  final Map<Class<? extends BaseCommand>, List<CommandHandlerMethod>> routes = new HashMap<>();

    /**
     * Agregar los nuevos mètodos quue vayan ingresando
     * @param type
     * @param handler
     * @param <T>
     */
    @Override
    public <T extends BaseCommand> void registerHandler(Class<T> type, CommandHandlerMethod<T> handler) {
        //routes method , llamado ComputeIfAbsent, agreag un nuevo valor y los asocia con una
        //especifica que aun no este asociada a un hasp map aun
        val handlers = routes.computeIfAbsent(type, c -> new LinkedList<>());
        handlers.add(handler);
    }

    @Override
    public void send(BaseCommand command) {
    // un command solo puede tener un handler
       val handlers= routes.get(command.getClass());
       //validar si tiene hadlers o no
        if(handlers == null || handlers.size() == 0){
            throw new RuntimeException("El command handler no fue registrado");
        }
        if(handlers.size()>1){
            throw new RuntimeException("No se puede enviar un command que tiene mas de un handler");
        }

        handlers.get(0).handle(command);
    }
}

