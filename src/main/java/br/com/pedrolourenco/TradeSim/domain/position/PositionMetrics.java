package br.com.pedrolourenco.TradeSim.domain.position;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PositionMetrics {
    private String name;

    private String ticker;

    private Long quantity;

    private BigDecimal marketPrice;

    private BigDecimal totalValue;

    private BigDecimal averagePrice;

    private BigDecimal floatingPnLValue;

    private BigDecimal floatingPnLPercentage;
}
