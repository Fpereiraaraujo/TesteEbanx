package com.FernandoPereira.TesteEbanx.framework.out.repositories;

import com.FernandoPereira.TesteEbanx.application.port.out.AccountPortOut;
import com.FernandoPereira.TesteEbanx.domain.dto.AccountDTO;
import com.FernandoPereira.TesteEbanx.framework.out.entities.AccountEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AccountRepositoryImp implements AccountPortOut {

    private final AccountRepository accountRepository;

    @Override
    public AccountDTO save(AccountDTO accountDTO) {

        AccountEntity entity = AccountDTO.toEntity(accountDTO);

        AccountEntity savedEntity = accountRepository.save(entity);


        return AccountDTO.toDTO(savedEntity);
    }

    @Override
    public void reset() {
        accountRepository.deleteAll();
    }

    @Override
    public Optional<AccountDTO> findById(String id) {
        Optional<AccountEntity> entityOptional = accountRepository.findById(id);


        return entityOptional.map(AccountDTO::toDTO);
    }
}