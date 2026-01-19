package com.FernandoPereira.TesteEbanx.framework.out.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


import java.math.BigDecimal;

@Data
@Entity
@Table(name = "accounts")
public class AccountEntity {

    @Id
    @Column(name = "account_id")
    private String id;

    @Column(name = "balance")
    private BigDecimal balance;

}
