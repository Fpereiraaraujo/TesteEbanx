package com.FernandoPereira.TesteEbanx.domain.dto;

import com.FernandoPereira.TesteEbanx.framework.out.entities.AccountEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {

    public static ModelMapper modelMapper = new ModelMapper();

    private String id;
    private BigDecimal balance;


    public static AccountEntity toEntity(AccountDTO accountDTO){
        return modelMapper.map(accountDTO, AccountEntity.class);
    }


    public static AccountDTO toDTO(AccountEntity accountEntity){
        return modelMapper.map(accountEntity, AccountDTO.class);
    }





}
