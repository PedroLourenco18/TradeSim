package br.com.pedrolourenco.TradeSim.mapper;

import br.com.pedrolourenco.TradeSim.domain.transaction.Transaction;
import br.com.pedrolourenco.TradeSim.domain.transaction.TransactionOutputDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionOutputDTO toDTO(Transaction transaction);
}
