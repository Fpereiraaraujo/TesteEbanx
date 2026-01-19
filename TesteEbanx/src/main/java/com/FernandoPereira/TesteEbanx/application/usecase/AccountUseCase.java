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
    public AccountDTO deposit(String destination, BigDecimal amount) {

        AccountDTO account = accountPortOut.findById(destination).orElse(null);


        if (account == null) {
            AccountDTO newAccount = new AccountDTO(destination, amount);
            return accountPortOut.save(newAccount);
        }


        BigDecimal newBalance = account.getBalance().add(amount);
        account.setBalance(newBalance);


        return accountPortOut.save(account);
    }

    @Override
    public void reset() {
        accountPortOut.reset();
    }
}