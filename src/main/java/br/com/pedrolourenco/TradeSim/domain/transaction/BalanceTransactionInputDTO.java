package br.com.pedrolourenco.TradeSim.domain.transaction;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(description = "Input DTO for performing a balance transaction (deposit/withdrawal).")
public class BalanceTransactionInputDTO {
    @Schema(description = "The amount to be processed. Must be a positive number with up to 2 decimal places.", example = "150.50")
    @NotBlank(message = "amount is missing")
    @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$", message = "Must be a number with up to 2 decimal places")
    private String amount;
}
