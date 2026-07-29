package br.com.pedrolourenco.TradeSim.domain.stock;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Data Transfer Object representing stock information.")
public class StockOutputDTO {

    @Schema(description = "The full name of the stock", example = "Petróleo Brasileiro S.A.")
    private String name;

    @Schema(description = "The stock ticker symbol", example = "PETR4")
    private String ticker;
}
