package br.com.pedrolourenco.TradeSim.domain.transaction;

import br.com.pedrolourenco.TradeSim.domain.stock.Stock;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionOutputDTO {
    private TransactionType type;

    private Stock stock;

    private Long quantity;

    private BigDecimal price;

    private BigDecimal fee;

    private BigDecimal amount;

    private LocalDateTime createdAt;
}
