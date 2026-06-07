package br.com.pedrolourenco.TradeSim.domain.user;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class UserOutputDTO {
    private String name;

    private String nickname;

    private String email;

    private String cpf;

    private LocalDate birthDate;

    private BigDecimal balance;
}
