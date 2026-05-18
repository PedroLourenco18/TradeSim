package br.com.pedrolourenco.TradeSim.domain.position;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PositionBasicInfo {
    private String name;

    private String ticker;

    private BigDecimal totalValue;

    private BigDecimal portfolioWeight;
}
