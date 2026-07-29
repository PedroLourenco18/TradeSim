package br.com.pedrolourenco.TradeSim.controller;

import br.com.pedrolourenco.TradeSim.controller.response.PageMetadata;
import br.com.pedrolourenco.TradeSim.controller.response.DataResponse;
import br.com.pedrolourenco.TradeSim.controller.response.PagedDataResponse;
import br.com.pedrolourenco.TradeSim.controller.response.BasicResponse;
import br.com.pedrolourenco.TradeSim.domain.stock.Stock;
import br.com.pedrolourenco.TradeSim.domain.stock.StockOutputDTO;
import br.com.pedrolourenco.TradeSim.domain.stock.StockPriceOutputDTO;
import br.com.pedrolourenco.TradeSim.exception.UnprocessableDataException;
import br.com.pedrolourenco.TradeSim.mapper.StockMapper;
import br.com.pedrolourenco.TradeSim.service.StockService;
import br.com.pedrolourenco.TradeSim.utils.ApiUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/stocks")
@RequiredArgsConstructor
@Tag(name = "Stocks", description = "Endpoints for querying available stocks for trading")
public class StockController {
    private final StockService stockService;

    private final StockMapper stockMapper;

    private final ApiUtils apiUtils;

    @GetMapping("/{ticker}")
    @Operation(
        summary = "Get stock by ticker",
        description = """
        Retrieves details and the current price of a specific stock by its ticker symbol.

        **🔒 Endpoint protected — requires JWT authentication.**
        """,
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Stock found successfully",
            content = @Content(
                schema = @Schema(implementation = DataResponse.class),
                examples = @ExampleObject(value = """
                {
                  "error": false,
                  "message": "Stock found",
                  "data": {
                    "name": "Petróleo Brasileiro S.A.",
                    "ticker": "PETR4",
                    "stockPrice": 35.50
                  }
                }
                """)
            )),
        @ApiResponse(responseCode = "404", description = "Stock not found or unavailable",
            content = @Content(
                schema = @Schema(implementation = BasicResponse.class),
                examples = @ExampleObject(value = """
                {
                  "error": true,
                  "message": "stock not available"
                }
                """)
            ))
    })
    public ResponseEntity<DataResponse<StockPriceOutputDTO>> findByTicker(@PathVariable String ticker){
        Stock stock = stockService.findActiveByTicker(ticker);

        BigDecimal currentStockPrice = stockService.getStockPrice(ticker);

        StockPriceOutputDTO stockOutput = new StockPriceOutputDTO(stock.getName(), ticker, currentStockPrice);

        DataResponse<StockPriceOutputDTO> response = new DataResponse<>(false, "Stock found", stockOutput);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(
        summary = "List all stocks",
        description = """
        Lists all available stocks in a paginated format.

        **🔒 Endpoint protected — requires JWT authentication.**
        """,
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Stocks listed successfully",
            content = @Content(
                schema = @Schema(implementation = PagedDataResponse.class),
                examples = @ExampleObject(value = """
                {
                  "error": false,
                  "message": "Stocks found",
                  "metadata": {
                    "pageNumber": 0,
                    "pageSize": 10,
                    "pageElements": 1,
                    "totalElements": 1,
                    "totalPages": 1,
                    "isFirst": true,
                    "isLast": true,
                    "hasNext": false,
                    "hasPrevious": false
                  },
                  "data": [
                    {
                      "name": "Petróleo Brasileiro S.A.",
                      "ticker": "PETR4"
                    }
                  ]
                }
                """)
            )),
        @ApiResponse(responseCode = "422", description = "Invalid pagination or sorting parameters",
            content = @Content(
                schema = @Schema(implementation = BasicResponse.class),
                examples = @ExampleObject(value = """
                {
                  "error": true,
                  "message": "Invalid sort field"
                }
                """)
            ))
    })
    public ResponseEntity<PagedDataResponse<StockOutputDTO>> list(@PageableDefault(page = 0, size = 10, direction = Sort.Direction.ASC)
                     Pageable pageable){
        pageable.getSort().map(Sort.Order::getProperty)
                .forEach(p -> {
                    if(!apiUtils.hasAttribute(Stock.class, p)){
                        throw new UnprocessableDataException("Invalid sort field");
                    }
                });

        Page<StockOutputDTO> page = stockService.listStocks(pageable)
                .map(stockMapper::toDTO);

        PageMetadata metadata = new PageMetadata(
                page.getNumber(),
                page.getSize(),
                page.getNumberOfElements(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast(),
                page.hasNext(),
                page.hasPrevious()
        );

        String message = page.getNumberOfElements() == 0 ?
                "No stocks found" : "Stocks found";

        PagedDataResponse<StockOutputDTO> response = new PagedDataResponse<>(
                false,
                message,
                metadata,
                page.getContent()
        );

        return ResponseEntity.ok(response);
    }
}
