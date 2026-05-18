package br.com.pedrolourenco.TradeSim.gateway;

import br.com.pedrolourenco.TradeSim.exception.InternalErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import tools.jackson.databind.JsonNode;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Component
public class BrapiStockDataGateway implements StockDataGateway {
    private final RestClient restClient;

    @Value("${brapi.url}")
    private String url;

    @Value("${brapi.token}")
    private String token;

    public BigDecimal getPrice(String ticker) {
        String uri = url + "/quote/" + ticker;
        String header = "Bearer " + token;

        try {
            return restClient.get()
                    .uri(uri)
                    .header("Authorization", header)
                    .retrieve()
                    .body(JsonNode.class)
                    .path("results")
                    .path(0)
                    .path("regularMarketPrice")
                    .decimalValue();
        }catch (NullPointerException | RestClientException e){
            throw new InternalErrorException("Houve um erro interno");
            //TODO log
        }
    }
}
