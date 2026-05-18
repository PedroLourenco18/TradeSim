package br.com.pedrolourenco.TradeSim.domain.transaction;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class StockTransactionInputDTO {
    @Positive
    private Long quantity;
}
