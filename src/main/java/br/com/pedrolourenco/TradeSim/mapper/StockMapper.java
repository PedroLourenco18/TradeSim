package br.com.pedrolourenco.TradeSim.mapper;

import br.com.pedrolourenco.TradeSim.domain.stock.Stock;
import br.com.pedrolourenco.TradeSim.domain.stock.StockOutputDTO;
import br.com.pedrolourenco.TradeSim.domain.stock.StockPriceOutputDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StockMapper {
    StockOutputDTO toDTO(Stock stock);
}
