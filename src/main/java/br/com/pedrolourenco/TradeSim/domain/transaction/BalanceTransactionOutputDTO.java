package br.com.pedrolourenco.TradeSim.domain.transaction;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class BalanceTransactionOutputDTO {
    private UUID transactionId;

    private TransactionType transactionType;

    private BigDecimal amount;

    private LocalDateTime transactionTime;
}
