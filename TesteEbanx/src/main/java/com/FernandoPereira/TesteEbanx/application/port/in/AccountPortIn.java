package com.FernandoPereira.TesteEbanx.application.port.in;

import com.FernandoPereira.TesteEbanx.domain.dto.AccountDTO;

import java.math.BigDecimal;

public interface AccountPortIn {
    AccountDTO getBalance(String accountId);
    AccountDTO deposit(String accountId, BigDecimal amount);
    AccountDTO withdraw(String accountId, BigDecimal amount);
    AccountDTO transfer(String originId, String destinationId, BigDecimal amount);
    void reset();
}
