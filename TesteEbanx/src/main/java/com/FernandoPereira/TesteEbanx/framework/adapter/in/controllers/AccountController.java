package com.FernandoPereira.TesteEbanx.framework.adapter.in.controllers;

import com.FernandoPereira.TesteEbanx.application.port.in.AccountPortIn;
import com.FernandoPereira.TesteEbanx.domain.dto.AccountDTO;
import com.FernandoPereira.TesteEbanx.domain.dto.EventRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Tag(name = "Ebanx API", description = "Operações de transações financeiras")
public class AccountController {

    private final AccountPortIn accountPortIn;

    @Operation(
            summary = "Resetar estado",
            description = "Limpa todos os dados para iniciar os testes automatizados"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @PostMapping("/reset")
    public ResponseEntity<String> reset() {
        accountPortIn.reset();
        return ResponseEntity.ok("OK");
    }

    @Operation(
            summary = "Consultar saldo",
            description = "Retorna o saldo de uma conta existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Saldo encontrado"),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada", content = @Content(schema = @Schema(implementation = Integer.class)))
    })
    @GetMapping("/balance")
    public ResponseEntity<Object> getBalance(@RequestParam("account_id") String accountId) {
        AccountDTO account = accountPortIn.getBalance(accountId);

        if (account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(0);
        }
        return ResponseEntity.ok(account.getBalance());
    }

    @Operation(
            summary = "Processar evento",
            description = "Realiza depósitos, saques e transferências"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Evento realizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada para saque ou transferência")
    })
    @PostMapping("/event")
    public ResponseEntity<Object> event(@RequestBody EventRequestDTO request) {
        try {
            if ("deposit".equals(request.getType())) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(accountPortIn.deposit(request.getDestination(), request.getAmount()));

            } else if ("withdraw".equals(request.getType())) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(accountPortIn.withdraw(request.getOrigin(), request.getAmount()));

            } else if ("transfer".equals(request.getType())) {
                AccountDTO origin = accountPortIn.transfer(
                        request.getOrigin(),
                        request.getDestination(),
                        request.getAmount()
                );

                AccountDTO destination = accountPortIn.getBalance(request.getDestination());

                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(Map.of("origin", origin, "destination", destination));
            }

            return ResponseEntity.badRequest().body("Invalid event type");

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(0);
        }
    }
}