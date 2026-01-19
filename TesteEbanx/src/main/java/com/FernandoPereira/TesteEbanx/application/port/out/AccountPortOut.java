package com.FernandoPereira.TesteEbanx.application.port.out;

import com.FernandoPereira.TesteEbanx.domain.dto.AccountDTO;

import java.util.Optional;

public interface AccountPortOut {
    Optional<AccountDTO> findById(String id);
    AccountDTO save(AccountDTO account);
    void reset();
}
