package br.com.pedrolourenco.TradeSim.domain.stock;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Schema(description = "Data Transfer Object representing stock price information.")
public class StockPriceOutputDTO {

    @Schema(description = "The full name of the stock", example = "Petróleo Brasileiro S.A.")
    private String name;

    @Schema(description = "The stock ticker symbol", example = "PETR4")
    private String ticker;

    @Schema(description = "The current price of the stock", example = "35.50")
    private BigDecimal stockPrice;
}
