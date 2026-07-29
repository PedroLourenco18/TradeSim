package br.com.pedrolourenco.TradeSim.domain.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

@Data
@Schema(description = "Data Transfer Object for registering a new user.")
public class RegisterUserInputDTO {
    @NotBlank(message = "Name cannot be blank")
    @Size(max = 150, message = "Name must have up to 150 characters")
    @Schema(description = "The user's full name.", example = "John Doe")
    private String name;

    @NotBlank(message = "Nickname cannot be blank")
    @Size(max = 30, message = "Nickname must have up to 30 characters")
    @Schema(description = "The user's nickname.", example = "johndoe")
    private String nickname;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Enter a valid email")
    @Schema(description = "The user's email address.", example = "john@example.com")
    private String email;

    @NotBlank(message = "CPF cannot be blank")
    @CPF(message = "Enter a valid CPF")
    @Pattern(regexp = "\\d{11}", message = "Enter a valid CPF")
    @Schema(description = "The user's CPF (11 digits).", example = "12345678901")
    private String cpf;

    @NotBlank(message = "Birth date cannot be blank")
    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$", message = "Date must be in yyyy-MM-dd format")
    @Schema(description = "The user's birth date in yyyy-MM-dd format.", example = "1990-01-01")
    private String birthDate;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 4, max = 20, message = "Password must be between 4 and 20 characters")
    @Schema(description = "The user's password.", example = "securePassword123")
    private String password;
}
