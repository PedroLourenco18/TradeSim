package br.com.pedrolourenco.TradeSim.domain.position;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Schema(description = "Data Transfer Object representing the performance metrics of a specific stock position.")
public class PositionMetrics {

    @Schema(description = "The full name of the company or asset.", example = "Petróleo Brasileiro S.A.")
    private String name;

    @Schema(description = "The stock ticker symbol.", example = "PETR4")
    private String ticker;

    @Schema(description = "The total number of shares held in this position.", example = "100")
    private Long quantity;

    @Schema(description = "The current market price per share.", example = "150.00")
    private BigDecimal marketPrice;

    @Schema(description = "The total current market value of the position (quantity * marketPrice).", example = "15000.00")
    private BigDecimal totalValue;

    @Schema(description = "The weighted average cost per share paid for the position. Calculated as the total cost of all purchases divided by the total quantity of shares.", example = "140.00")
    private BigDecimal averagePrice;

    @Schema(description = "The unrealized profit or loss in monetary terms. Calculated as (marketPrice - averagePrice) * quantity.", example = "1000.00")
    private BigDecimal floatingPnLValue;

    @Schema(description = "The unrealized profit or loss expressed as a percentage. Calculated as ((marketPrice - averagePrice) / averagePrice) * 100.", example = "7.14")
    private BigDecimal floatingPnLPercentage;
}
