package br.com.pedrolourenco.TradeSim.gateway;

import java.math.BigDecimal;

public interface StockDataGateway {
    BigDecimal getPrice (String ticket);
}
