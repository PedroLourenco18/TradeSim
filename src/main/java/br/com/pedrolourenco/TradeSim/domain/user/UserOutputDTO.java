package br.com.pedrolourenco.TradeSim.domain.user;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class UserOutputDTO {
    private UUID id;

    private String name;

    private String nickname;

    private String email;

    private String cpf;

    private LocalDate birthDate;

    private BigDecimal balance;
}
