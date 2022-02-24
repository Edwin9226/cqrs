package com.banking.account.cmd.api.command;

import com.banking.cqrs.common.commands.BaseCommand;
import lombok.Data;

@Data
public class DepositFundsCommand extends BaseCommand {
    private double amount;
}
