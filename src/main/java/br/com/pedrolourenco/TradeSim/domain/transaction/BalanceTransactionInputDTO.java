package br.com.pedrolourenco.TradeSim.domain.transaction;

import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BalanceTransactionInputDTO {
    @Positive
    private BigDecimal amount;
}
