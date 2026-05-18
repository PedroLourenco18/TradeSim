package br.com.pedrolourenco.TradeSim.controller;

import br.com.pedrolourenco.TradeSim.domain.position.PortfolioMetrics;
import br.com.pedrolourenco.TradeSim.domain.position.PositionMetrics;
import br.com.pedrolourenco.TradeSim.controller.response.DataResponse;
import br.com.pedrolourenco.TradeSim.domain.user.User;
import br.com.pedrolourenco.TradeSim.security.CustomUserDetails;
import br.com.pedrolourenco.TradeSim.service.PositionService;
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
public class PortfolioController {
    private final PositionService positionService;

    @GetMapping("/position/{ticker}")
    public ResponseEntity<DataResponse<PositionMetrics>> getPosition(@PathVariable String ticker){
        PositionMetrics positionMetrics = positionService.getPositionMetrics(getAuthenticatedUser(), ticker);

        DataResponse<PositionMetrics> response = new DataResponse<>(
                false,
                "posição encontrada",
                positionMetrics);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/portfolio")
    public ResponseEntity<DataResponse<PortfolioMetrics>> getPortfolio(){
        PortfolioMetrics portfolioMetrics = positionService.getPortfolioMetrics(getAuthenticatedUser());

        DataResponse<PortfolioMetrics> response = new DataResponse<>(
                false,
                "portfolio encontrado",
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
