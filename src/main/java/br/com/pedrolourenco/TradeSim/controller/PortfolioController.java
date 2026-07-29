package br.com.pedrolourenco.TradeSim.controller;

import br.com.pedrolourenco.TradeSim.controller.response.BasicResponse;
import br.com.pedrolourenco.TradeSim.controller.response.DataResponse;
import br.com.pedrolourenco.TradeSim.domain.position.PortfolioMetrics;
import br.com.pedrolourenco.TradeSim.domain.position.PositionMetrics;
import br.com.pedrolourenco.TradeSim.domain.user.User;
import br.com.pedrolourenco.TradeSim.security.CustomUserDetails;
import br.com.pedrolourenco.TradeSim.service.PositionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
@RequiredArgsConstructor
@Tag(name = "Portfolio", description = "Endpoints for managing and viewing user portfolio and positions")
public class PortfolioController {
    private final PositionService positionService;

    @GetMapping("/position/{ticker}")
    @Operation(
        summary = "Get position details by ticker",
        description = """
        Retrieves detailed performance metrics for a specific stock position.
        - **Average Price**: The weighted average cost per share.
        - **Floating PnL Value**: The unrealized profit or loss in monetary terms.
        - **Floating PnL Percentage**: The unrealized profit or loss as a percentage.

        **🔒 Endpoint protected — requires JWT authentication.**
        """,
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Position found successfully",
            content = @Content(
                schema = @Schema(implementation = DataResponse.class),
                examples = @ExampleObject(value = """
                {
                  "error": false,
                  "message": "Position found",
                  "data": {
                    "name": "Petróleo Brasileiro S.A.",
                    "ticker": "PETR4",
                    "quantity": 100,
                    "marketPrice": 150.00,
                    "totalValue": 15000.00,
                    "averagePrice": 140.00,
                    "floatingPnLValue": 1000.00,
                    "floatingPnLPercentage": 7.14
                  }
                }
                """)
            )),
        @ApiResponse(responseCode = "404", description = "Position not found for the given ticker",
            content = @Content(
                schema = @Schema(implementation = BasicResponse.class),
                examples = @ExampleObject(value = """
                {
                  "error": true,
                  "message": "Position not found for ticker"
                }
                """)
            ))
    })
    public ResponseEntity<DataResponse<PositionMetrics>> getPosition(@PathVariable String ticker){
        PositionMetrics positionMetrics = positionService.getPositionMetrics(getAuthenticatedUser(), ticker);

        DataResponse<PositionMetrics> response = new DataResponse<>(
                false,
                "Position found",
                positionMetrics);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/portfolio")
    @Operation(
        summary = "Get full portfolio metrics",
        description = """
        Retrieves aggregated performance metrics for the entire portfolio.
        - **Total Value**: The sum of the market value of all positions.
        - **Total Floating PnL Value**: The sum of unrealized profit or loss across all positions.
        - **Total Floating PnL Percentage**: The overall portfolio return percentage.

        **🔒 Endpoint protected — requires JWT authentication.**
        """,
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Portfolio found successfully",
            content = @Content(
                schema = @Schema(implementation = DataResponse.class),
                examples = @ExampleObject(value = """
                {
                  "error": false,
                  "message": "Portfolio found",
                  "data": {
                    "totalValue": 50000.00,
                    "totalFloatingPnLValue": 2500.00,
                    "totalFloatingPnLPercentage": 5.25,
                    "positions": [
                      {
                        "ticker": "PETR4",
                        "quantity": 100
                      }
                    ]
                  }
                }
                """)
            ))
    })
    public ResponseEntity<DataResponse<PortfolioMetrics>> getPortfolio(){
        PortfolioMetrics portfolioMetrics = positionService.getPortfolioMetrics(getAuthenticatedUser());

        DataResponse<PortfolioMetrics> response = new DataResponse<>(
                false,
                "Portfolio found",
                portfolioMetrics);

        return ResponseEntity.ok(response);
    }

    private User getAuthenticatedUser(){
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = new User();
        user.setId(userDetails.getId());

        return user;
    }
}
