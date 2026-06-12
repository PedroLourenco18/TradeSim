package br.com.pedrolourenco.TradeSim.domain.transaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BalanceTransactionInputDTO {
    @NotBlank(message = "amount faltando")
    @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$", message = "Deve ser um número com no máximo 2 casas decimais")
    private String amount;
}
