package com.FernandoPereira.TesteEbanx.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class EventRequestDTO {
    private String type;
    private String destination;
    private String origin;
    private BigDecimal amount;
}
