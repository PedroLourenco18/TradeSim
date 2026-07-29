package br.com.pedrolourenco.TradeSim.domain.transaction;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Schema(description = "Data Transfer Object representing a stock transaction details.")
public class TransactionOutputDTO {
    @Schema(description = "Unique identifier of the transaction.", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID transactionId;

    @Schema(description = "The type of the transaction (e.g., BUY, SELL).")
    private TransactionType transactionType;

    @Schema(description = "The ticker symbol of the stock involved in the transaction.", example = "PETR4")
    private String stockTicker;

    @Schema(description = "The quantity of stocks traded.", example = "100")
    private Long stockQuantity;

    @Schema(description = "The price per stock unit at the time of the transaction.", example = "25.50")
    private BigDecimal stockPrice;

    @Schema(description = "The fee charged for the transaction.", example = "0.50")
    private BigDecimal fee;

    @Schema(description = "The total monetary amount of the transaction.", example = "2550.00")
    private BigDecimal amount;

    @Schema(description = "The timestamp when the transaction occurred.", example = "2023-10-27T10:00:00")
    private LocalDateTime transactionTime;
}
