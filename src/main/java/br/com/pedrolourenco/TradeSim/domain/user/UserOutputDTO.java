package br.com.pedrolourenco.TradeSim.domain.user;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO (Data Transfer Object) utilizado para representar os dados de um usuário
 * que serão retornados na resposta da API.
 */
@Data
public class UserOutputDTO {
    /**
     * Nome completo do usuário.
     */
    private String name;

    /**
     * Apelido ou nome de exibição do usuário.
     */
    private String nickname;

    /**
     * Endereço de e-mail do usuário.
     */
    private String email;

    /**
     * CPF do usuário.
     */
    private String cpf;

    /**
     * Data de nascimento do usuário.
     */
    private LocalDate birthDate;

    /**
     * Saldo atual disponível na conta do usuário.
     */
    private BigDecimal balance;
}
