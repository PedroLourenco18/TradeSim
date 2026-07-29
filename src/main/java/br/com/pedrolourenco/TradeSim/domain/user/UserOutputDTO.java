package br.com.pedrolourenco.TradeSim.domain.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Schema(description = "Data Transfer Object representing user information returned by the API.")
public class UserOutputDTO {

    @Schema(description = "The full name of the user.", example = "John Doe")
    private String name;

    @Schema(description = "The user's nickname or display name.", example = "johndoe")
    private String nickname;

    @Schema(description = "The user's email address.", example = "john.doe@example.com")
    private String email;

    @Schema(description = "The user's CPF (Brazilian tax ID) as a numeric string.", example = "12345678900")
    private String cpf;

    @Schema(description = "The user's date of birth.", example = "1990-01-01")
    private LocalDate birthDate;

    @Schema(description = "The current available balance in the user's account.", example = "1500.50")
    private BigDecimal balance;
}
