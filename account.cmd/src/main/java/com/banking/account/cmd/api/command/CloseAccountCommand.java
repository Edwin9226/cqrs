package com.banking.account.cmd.api.command;

import com.banking.cqrs.common.commands.BaseCommand;

public class CloseAccountCommand extends BaseCommand {
    public CloseAccountCommand(String id)
    {
        super(id);
    }
}
