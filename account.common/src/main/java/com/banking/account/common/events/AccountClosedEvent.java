package com.banking.account.common.events;

import com.banking.cqrs.common.events.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountClosedEvent extends BaseEvent {

}
