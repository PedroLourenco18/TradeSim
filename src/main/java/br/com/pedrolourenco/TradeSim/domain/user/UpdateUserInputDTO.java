package br.com.pedrolourenco.TradeSim.domain.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserInputDTO {
    @NotBlank(message = "O apelido não pode estar em branco")
    @Size(max = 30, message = "O apelido deve ter até 30 caracteres")
    private String nickname;

    @NotBlank(message = "O email não pode estar em branco")
    @Email(message = "Insira um email válido")
    private String email;
}
