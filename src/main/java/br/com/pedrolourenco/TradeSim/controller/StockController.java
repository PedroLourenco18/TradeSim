package br.com.pedrolourenco.TradeSim.controller;

import br.com.pedrolourenco.TradeSim.controller.response.DataListMetadata;
import br.com.pedrolourenco.TradeSim.controller.response.DataListResponse;
import br.com.pedrolourenco.TradeSim.controller.response.DataResponse;
import br.com.pedrolourenco.TradeSim.domain.stock.Stock;
import br.com.pedrolourenco.TradeSim.domain.stock.StockOutputDTO;
import br.com.pedrolourenco.TradeSim.domain.stock.StockPriceOutputDTO;
import br.com.pedrolourenco.TradeSim.mapper.StockMapper;
import br.com.pedrolourenco.TradeSim.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/stocks")
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;

    private final StockMapper stockMapper;

    @GetMapping("/{ticker}")
    public ResponseEntity<DataResponse<StockPriceOutputDTO>> findByTicker(@PathVariable String ticker){
        Stock stock = stockService.findByTicker(ticker);

        BigDecimal currentStockPrice = stockService.getStockPrice(ticker);

        StockPriceOutputDTO stockOutput = new StockPriceOutputDTO(stock.getName(), ticker, currentStockPrice);

        DataResponse<StockPriceOutputDTO> response = new DataResponse<>(false, "ação encontrada", stockOutput);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<DataListResponse<StockOutputDTO>> list(@PageableDefault(page = 0, size = 10, direction = Sort.Direction.ASC)
                     Pageable pageable){
        Page<StockOutputDTO> page = stockService.listStocks(pageable)
                .map(stockMapper::toDTO);

        DataListMetadata metadata = new DataListMetadata(
                page.getPageable().getPageNumber(),
                page.getSize(),
                page.getTotalPages(),
                page.getNumberOfElements());

        DataListResponse<StockOutputDTO> response = new DataListResponse<>(false, metadata, page);

        return ResponseEntity.ok(response);
    }
}
