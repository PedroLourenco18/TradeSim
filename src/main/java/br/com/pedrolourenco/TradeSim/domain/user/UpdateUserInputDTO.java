package br.com.pedrolourenco.TradeSim.domain.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Data Transfer Object for updating user information.")
public class UpdateUserInputDTO {
    @NotBlank(message = "Nickname cannot be blank")
    @Size(max = 30, message = "Nickname must have up to 30 characters")
    @Schema(description = "The user's new nickname.", example = "pedro_dev")
    private String nickname;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Enter a valid email")
    @Schema(description = "The user's new email address.", example = "pedro@example.com")
    private String email;
}
