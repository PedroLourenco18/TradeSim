package br.com.pedrolourenco.TradeSim.mapper;

import br.com.pedrolourenco.TradeSim.domain.transaction.BalanceTransactionOutputDTO;
import br.com.pedrolourenco.TradeSim.domain.transaction.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BalanceTransactionMapper {
    @Mapping(source = "id", target = "transactionId")
    @Mapping(source = "type", target = "transactionType")
    @Mapping(source = "createdAt", target = "transactionTime")
    @Mapping(target = "amount", expression = "java( transaction.getAmount().setScale(2, java.math.RoundingMode.HALF_EVEN) )")
    BalanceTransactionOutputDTO toDTO(Transaction transaction);
}
