package br.com.pedrolourenco.TradeSim.service;

import br.com.pedrolourenco.TradeSim.domain.stock.Stock;
import br.com.pedrolourenco.TradeSim.exception.ResourceNotFoundException;
import br.com.pedrolourenco.TradeSim.gateway.StockDataGateway;
import br.com.pedrolourenco.TradeSim.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StockService {
    private final StockRepository stockRepository;

    private final StockDataGateway stockDataGateway;

    public Stock findByTicker(String ticker){
        Optional<Stock> stockOptional = stockRepository.findByTickerAndActiveIsTrue(ticker);

        if(stockOptional.isEmpty()){
            throw new ResourceNotFoundException("Essa ação não esta disponível nessa plataforma");
        }

        return stockOptional.get();
    }

    public Page<Stock> listStocks(Pageable pageable){
        return stockRepository.findAll(pageable);
    }

    public BigDecimal getStockPrice(String ticker){
        return stockDataGateway.getPrice(ticker);
    }
}
