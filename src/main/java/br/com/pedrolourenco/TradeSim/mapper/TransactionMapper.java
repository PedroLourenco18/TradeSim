package br.com.pedrolourenco.TradeSim.mapper;

import br.com.pedrolourenco.TradeSim.domain.transaction.Transaction;
import br.com.pedrolourenco.TradeSim.domain.transaction.TransactionOutputDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    @Mapping(source = "id", target = "transactionId")
    @Mapping(source = "type", target = "transactionType")
    @Mapping(source = "stock.ticker", target = "stockTicker")
    @Mapping(source = "quantity", target = "stockQuantity")
    @Mapping(target = "stockPrice", expression = "java( transaction.getPrice() == null ? null : transaction.getPrice().setScale(2, java.math.RoundingMode.HALF_EVEN) )")
    @Mapping(source = "createdAt", target = "transactionTime")
    @Mapping(target = "amount", expression = "java( transaction.getAmount() == null ? null : transaction.getAmount().setScale(2, java.math.RoundingMode.HALF_EVEN) )")
    @Mapping(target = "fee", expression = "java( transaction.getFee() == null ? null : transaction.getFee().setScale(2, java.math.RoundingMode.HALF_EVEN) )")
    TransactionOutputDTO toDTO(Transaction transaction);
}
