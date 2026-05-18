package br.com.pedrolourenco.TradeSim.domain.user;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

@Data
public class RegisterUserInputDTO {
    @NotBlank(message = "O nome não pode estar em branco")
    @Size(max = 150, message = "O nome deve ter até 150 caracteres")
    private String name;

    @NotBlank(message = "O apelido não pode estar em branco")
    @Size(max = 30, message = "O apelido deve ter até 30 caracteres")
    private String nickname;

    @NotBlank(message = "O email não pode estar em branco")
    @Email(message = "Insira um email válido")
    private String email;

    @NotBlank(message = "O cpf não pode estar em branco")
    @CPF(message = "Insira um cpf válido")
    private String cpf;

    @NotNull(message = "A data de nascimento não pode estar em branco")
    @Past(message = "Insira uma data de nascimento válida")
    private LocalDate birthDate;

    @NotBlank(message = "A senha não pode estar em branco")
    @Size(min = 4, max = 20, message = "A senha deve ter entre 4 e 20 caracteres")
    private String password;
}
