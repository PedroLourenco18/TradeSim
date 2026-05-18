package br.com.pedrolourenco.TradeSim.domain.stock;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class StockPriceOutputDTO {
    private String name;

    private String ticker;

    private BigDecimal stockPrice;
}
