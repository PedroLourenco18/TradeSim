package br.com.pedrolourenco.TradeSim.domain.transaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class StockTransactionInputDTO {
    @NotBlank(message = "quantity faltando")
    @Pattern(regexp = "^\\d+$", message = "Deve ser um número inteiro positivo")
    private String quantity;
}
