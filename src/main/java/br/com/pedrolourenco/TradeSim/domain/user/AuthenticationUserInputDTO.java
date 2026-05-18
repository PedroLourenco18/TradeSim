package br.com.pedrolourenco.TradeSim.domain.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

@Data
public class AuthenticationUserInputDTO {
    @NotBlank(message = "O cpf não pode estar em branco")
    @CPF(message = "Insira um cpf válido")
    private String cpf;

    @NotBlank(message = "A senha não pode estar em branco")
    @Size(min = 4, max = 20, message = "A senha deve ter entre 4 e 20 caracteres")
    private String password;
}
