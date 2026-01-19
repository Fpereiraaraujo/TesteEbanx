package com.FernandoPereira.TesteEbanx.framework.out.repositories;

import com.FernandoPereira.TesteEbanx.framework.out.entities.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountEntity, String> {
}
