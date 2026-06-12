package br.com.pedrolourenco.TradeSim.domain.user;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

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
    @Pattern(regexp = "\\d{11}", message = "Insira um cpf válido")
    private String cpf;

    @NotBlank(message = "A data de nascimento não pode estar em branco")
    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$", message = "Data deve estar no formato yyyy-MM-dd")
    private String birthDate;

    @NotBlank(message = "A senha não pode estar em branco")
    @Size(min = 4, max = 20, message = "A senha deve ter entre 4 e 20 caracteres")
    private String password;
}
