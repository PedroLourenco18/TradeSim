package br.com.pedrolourenco.TradeSim.domain.position;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@Schema(description = "Data Transfer Object representing the aggregated performance metrics of the entire portfolio.")
public class PortfolioMetrics {

    @Schema(description = "The total current market value of the entire portfolio. Calculated as the sum of the market value of all individual positions.", example = "50000.00")
    private BigDecimal totalValue;

    @Schema(description = "The total unrealized profit or loss of the portfolio in monetary terms. Calculated as the sum of the floating PnL of all individual positions.", example = "2500.00")
    private BigDecimal totalFloatingPnLValue;

    @Schema(description = "The total unrealized profit or loss of the portfolio expressed as a percentage. Calculated as (totalFloatingPnLValue / totalInvestedCost) * 100.", example = "5.25")
    private BigDecimal totalFloatingPnLPercentage;

    @Schema(description = "List of basic information for each position held in the portfolio.")
    private List<PositionBasicInfo> positions;
}
