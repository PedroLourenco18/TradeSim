package br.com.pedrolourenco.TradeSim.domain.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

@Data
@Schema(description = "Data Transfer Object for user authentication.")
public class AuthenticationUserInputDTO {

    @NotBlank(message = "CPF cannot be blank")
    @CPF(message = "Enter a valid CPF")
    @Pattern(regexp = "\\d{11}", message = "Enter a valid CPF")
    @Schema(description = "The user's CPF (11 digits)", example = "12345678901")
    private String cpf;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 4, max = 20, message = "Password must be between 4 and 20 characters")
    @Schema(description = "The user's password", example = "password123")
    private String password;
}
