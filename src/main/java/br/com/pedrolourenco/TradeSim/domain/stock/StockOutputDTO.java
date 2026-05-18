package br.com.pedrolourenco.TradeSim.domain.stock;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockOutputDTO {
    private String name;

    private String ticker;
}
