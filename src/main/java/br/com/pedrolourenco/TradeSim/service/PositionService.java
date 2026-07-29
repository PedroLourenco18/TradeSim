package br.com.pedrolourenco.TradeSim.service;

import br.com.pedrolourenco.TradeSim.domain.position.PortfolioMetrics;
import br.com.pedrolourenco.TradeSim.domain.position.Position;
import br.com.pedrolourenco.TradeSim.domain.position.PositionBasicInfo;
import br.com.pedrolourenco.TradeSim.domain.position.PositionMetrics;
import br.com.pedrolourenco.TradeSim.domain.stock.Stock;
import br.com.pedrolourenco.TradeSim.domain.transaction.Transaction;
import br.com.pedrolourenco.TradeSim.domain.user.User;
import br.com.pedrolourenco.TradeSim.exception.InternalErrorException;
import br.com.pedrolourenco.TradeSim.exception.ResourceNotFoundException;
import br.com.pedrolourenco.TradeSim.repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PositionService {
    private final PositionRepository positionRepository;

    private final StockService stockService;

    public PositionMetrics getPositionMetrics(User user, String ticker){
        Stock stock = stockService.findByTicker(ticker);

        Optional<Position> positionOptional = positionRepository.findByUserAndStock(user, stock);

        if(positionOptional.isEmpty()){
            throw new ResourceNotFoundException("User does not own this stock");
        }

        Position position = positionOptional.get();

        BigDecimal stockPrice = stockService.getStockPrice(ticker);

        BigDecimal totalValue = stockPrice.multiply(BigDecimal.valueOf(position.getQuantity()));

        BigDecimal floatingPnLValue = stockPrice
                .subtract(position.getAveragePrice())
                .multiply(BigDecimal.valueOf(position.getQuantity()));

        BigDecimal floatingPnLPercentage = stockPrice
                .subtract(position.getAveragePrice())
                .divide(stockPrice, new MathContext(16, RoundingMode.HALF_EVEN))
                .multiply(BigDecimal.valueOf(100));

        return new PositionMetrics(stock.getName(),
                ticker,
                position.getQuantity(),
                stockPrice.setScale(2, RoundingMode.HALF_EVEN),
                totalValue.setScale(2, RoundingMode.HALF_EVEN),
                position.getAveragePrice().setScale(2, RoundingMode.HALF_EVEN),
                floatingPnLValue.setScale(2, RoundingMode.HALF_EVEN),
                floatingPnLPercentage.setScale(2, RoundingMode.HALF_EVEN));
    }

    public PortfolioMetrics getPortfolioMetrics(User user){
        List<Position> positions = positionRepository.findByUser(user);

        BigDecimal portfolioAveragePrice = BigDecimal.ZERO;

        BigDecimal portfolioTotalValue = BigDecimal.ZERO;

        List<PositionBasicInfo> positionsInfo = new LinkedList<>();

        for(Position position:positions){
            BigDecimal stockPrice = stockService.getStockPrice(position.getStock().getTicker());

            BigDecimal positionTotalValue = stockPrice.multiply(BigDecimal.valueOf(position.getQuantity()));

            BigDecimal positionAveragePrice = position.getAveragePrice().multiply(BigDecimal.valueOf(position.getQuantity()));

            positionsInfo.add(new PositionBasicInfo(
                    position.getStock().getName(),
                    position.getStock().getTicker(),
                    positionTotalValue.setScale(2, RoundingMode.HALF_EVEN),
                    null));

            portfolioAveragePrice = portfolioAveragePrice.add(positionAveragePrice);

            portfolioTotalValue = portfolioTotalValue.add(positionTotalValue);
        }

        BigDecimal portfolioFloatingPnLValue = portfolioTotalValue.subtract(portfolioAveragePrice);

        BigDecimal portfolioFloatingPnLPercentage = portfolioFloatingPnLValue
                .divide(portfolioAveragePrice, new MathContext(16, RoundingMode.HALF_EVEN))
                .multiply(BigDecimal.valueOf(100));

        for(PositionBasicInfo position:positionsInfo){
            position.setPortfolioWeight(position.getTotalValue()
                            .divide(portfolioTotalValue, new MathContext(16, RoundingMode.HALF_EVEN))
                            .multiply(BigDecimal.valueOf(100))
                            .setScale(2, RoundingMode.HALF_EVEN));
        }

        return new PortfolioMetrics(
                portfolioTotalValue.setScale(2, RoundingMode.HALF_EVEN),
                portfolioFloatingPnLValue.setScale(2, RoundingMode.HALF_EVEN),
                portfolioFloatingPnLPercentage.setScale(2, RoundingMode.HALF_EVEN),
                positionsInfo);
    }

    public void stockPurchase(Transaction transaction){
        Optional<Position> positionOptional = positionRepository
                .findByUserAndStock(transaction.getUser(), transaction.getStock());

        if(positionOptional.isPresent()){
            Position position = positionOptional.get();

            BigDecimal averagePrice =
                    position.getAveragePrice().multiply(BigDecimal.valueOf(position.getQuantity()))
                    .add(transaction.getPrice().multiply(BigDecimal.valueOf(transaction.getQuantity())))
                    .divide(BigDecimal.valueOf(position.getQuantity() + transaction.getQuantity()), 4, RoundingMode.HALF_EVEN);

            position.setQuantity(position.getQuantity() + transaction.getQuantity());

            position.setAveragePrice(averagePrice);

            positionRepository.save(position);

            return;
        }

        Position position = new Position();
        position.setUser(transaction.getUser());
        position.setStock(transaction.getStock());
        position.setQuantity(transaction.getQuantity());
        position.setAveragePrice(transaction.getPrice().setScale(4, RoundingMode.HALF_EVEN));
        positionRepository.save(position);
    }

    public void stockSale(Transaction transaction){
        Optional<Position> positionOptional = positionRepository
                .findByUserAndStock(transaction.getUser(), transaction.getStock());

        if(positionOptional.isEmpty()){
            throw new InternalErrorException("An internal error occurred");
        }

        Position position = positionOptional.get();

        if(position.getQuantity().equals(transaction.getQuantity())){
            positionRepository.delete(position);
            return;
        }

        position.setQuantity(position.getQuantity() - transaction.getQuantity());
        positionRepository.save(position);
    }
}
