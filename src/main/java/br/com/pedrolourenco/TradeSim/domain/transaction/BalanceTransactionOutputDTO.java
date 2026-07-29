package br.com.pedrolourenco.TradeSim.domain.transaction;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Schema(description = "Data Transfer Object representing a balance-related transaction.")
public class BalanceTransactionOutputDTO {
    @Schema(description = "Unique identifier of the transaction.", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID transactionId;

    @Schema(description = "The type of the transaction (e.g., DEPOSIT, WITHDRAW).")
    private TransactionType transactionType;

    @Schema(description = "The monetary amount involved in the transaction.", example = "1000.00")
    private BigDecimal amount;

    @Schema(description = "The timestamp when the transaction occurred.", example = "2023-10-27T10:00:00")
    private LocalDateTime transactionTime;
}
