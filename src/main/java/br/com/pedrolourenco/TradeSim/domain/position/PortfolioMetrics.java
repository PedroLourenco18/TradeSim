package br.com.pedrolourenco.TradeSim.domain.position;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class PortfolioMetrics {
    private BigDecimal totalValue;

    private BigDecimal totalFloatingPnLValue;

    private BigDecimal totalFloatingPnLPercentage;

    private List<PositionBasicInfo> positions;
}
