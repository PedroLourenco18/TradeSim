package br.com.pedrolourenco.TradeSim.domain.transaction;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(description = "Input DTO for performing a stock transaction.")
public class StockTransactionInputDTO {
    @Schema(description = "The quantity of stocks to trade. Must be a positive integer.", example = "10")
    @NotBlank(message = "quantity is missing")
    @Pattern(regexp = "^\\d+$", message = "Must be a positive integer")
    private String quantity;
}
