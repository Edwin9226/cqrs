package com.banking.account.cmd;

import com.banking.account.cmd.api.command.*;
import com.banking.cqrs.common.infrastructure.CommandDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class CommandApplication {
	/**
	 * Inyectar los objetos CommandDispatcher y CommandHandler.
	 * @param args
	 */
	@Autowired
	private CommandDispatcher commandDispatcher;

	@Autowired
	private CommandHandler commandHandler;

	public static void main(String[] args) {
		SpringApplication.run(CommandApplication.class, args);
	}

	// acontinuaciòn crearemos un mètodo que se ejecute ed inmediato despuès del
	//constructor
	@PostConstruct
	public void registerHandler(){
	commandDispatcher.registerHandler(OpenAccountCommand.class, commandHandler::handle);
	commandDispatcher.registerHandler(DepositFundsCommand.class, commandHandler::handle);
	commandDispatcher.registerHandler(WithdrawFundsCommand.class, commandHandler::handle);
	commandDispatcher.registerHandler(CloseAccountCommand.class, commandHandler::handle);

	}


}
