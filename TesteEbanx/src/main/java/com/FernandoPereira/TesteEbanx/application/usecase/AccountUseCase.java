package com.FernandoPereira.TesteEbanx.application.usecase;

import com.FernandoPereira.TesteEbanx.application.port.in.AccountPortIn;
import com.FernandoPereira.TesteEbanx.application.port.out.AccountPortOut;
import com.FernandoPereira.TesteEbanx.domain.dto.AccountDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountUseCase implements AccountPortIn {

    private final AccountPortOut accountPortOut;


    @Override
    public AccountDTO getBalance(String accountId) {
        return accountPortOut.findById(accountId)
                .orElse(null);
    }

    @Override
    public AccountDTO deposit(String destination, BigDecimal amount) {
        // CORREÇÃO: Usamos .orElse(null) para pegar o valor ou nulo se não existir
        AccountDTO account = accountPortOut.findById(destination).orElse(null);

        // 2. Se a conta NÃO existir (for null), cria uma nova
        if (account == null) {
            AccountDTO newAccount = new AccountDTO(destination, amount);
            return accountPortOut.save(newAccount);
        }

        // 3. Se a conta JÁ existir, soma o valor ao saldo atual
        BigDecimal newBalance = account.getBalance().add(amount);
        account.setBalance(newBalance);

        // 4. Salva a atualização e retorna
        return accountPortOut.save(account);
    }


    @Override
    public AccountDTO withdraw(String accountId, BigDecimal amount) {

        AccountDTO account = accountPortOut.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        BigDecimal newBalance = account.getBalance().subtract(amount);
        account.setBalance(newBalance);

        return accountPortOut.save(account);
    }


    @Override
    public AccountDTO transfer(String originId, String destinationId, BigDecimal amount) {

        AccountDTO originAccount = this.withdraw(originId, amount);

        this.deposit(destinationId, amount);


        return originAccount;
    }

    @Override
    public void reset() {
        accountPortOut.reset();
    }
}