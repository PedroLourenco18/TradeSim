package br.com.pedrolourenco.TradeSim.service;

import br.com.pedrolourenco.TradeSim.domain.stock.Stock;
import br.com.pedrolourenco.TradeSim.domain.balance_ledger.BalanceLedgerType;
import br.com.pedrolourenco.TradeSim.domain.position_ledger.PositionLedgerType;
import br.com.pedrolourenco.TradeSim.domain.transaction.Transaction;
import br.com.pedrolourenco.TradeSim.domain.transaction.TransactionType;
import br.com.pedrolourenco.TradeSim.domain.user.User;
import br.com.pedrolourenco.TradeSim.exception.UnprocessableDataException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransactionOrchestratorService {
    private final TransactionService transactionService;

    private final BalanceLedgerService balanceLedgerService;

    private final UserService userService;

    private final StockService stockService;

    private final PositionService positionService;

    private final PositionLedgerService positionLedgerService;

    @Transactional
    public Transaction withdraw(User user, BigDecimal amount){
        if(amount.compareTo(balanceLedgerService.calculateBalance(user)) > 0){
            throw new UnprocessableDataException(
                    "Cannot withdraw an amount greater than your balance");
        }

        userService.sumToBalance(user, amount.negate());

        Transaction savedTransaction = transactionService.save(user, TransactionType.WITHDRAW, amount);

        balanceLedgerService.save(savedTransaction, BalanceLedgerType.DEBIT);

        return savedTransaction;
    }

    @Transactional
    public Transaction deposit(User user, BigDecimal amount){
        userService.sumToBalance(user, amount);

        Transaction savedTransaction = transactionService.save(user, TransactionType.DEPOSIT, amount);

        balanceLedgerService.save(savedTransaction, BalanceLedgerType.CREDIT);

        return savedTransaction;
    }

    @Transactional
    public Transaction buyStock(User user, String stockTicker, Long quantity){
        Stock stock = stockService.findActiveByTicker(stockTicker);

        BigDecimal stockPrice = stockService.getStockPrice(stockTicker);

        BigDecimal purchasePrice = stockPrice.multiply(BigDecimal.valueOf(quantity));

        if(purchasePrice.compareTo(balanceLedgerService.calculateBalance(user)) > 0){
            throw new UnprocessableDataException(
                    "Cannot make a purchase with an amount greater than the balance");
        }

        Transaction savedTransaction = transactionService.save(user, TransactionType.STOCK_BUY, stock, quantity, stockPrice, purchasePrice, BigDecimal.ZERO);

        positionService.stockPurchase(savedTransaction);

        positionLedgerService.save(savedTransaction, PositionLedgerType.BUY);

        balanceLedgerService.save(savedTransaction, BalanceLedgerType.DEBIT);

        userService.sumToBalance(user, purchasePrice.negate());

        return savedTransaction;
    }

    @Transactional
    public Transaction sellStock(User user, String stockTicker, Long quantity){
        Stock stock = stockService.findByTicker(stockTicker);

        Long usersPortfolioQuantity = positionLedgerService.calculateStockQuantity(user, stock);

        if(quantity.compareTo(usersPortfolioQuantity) > 0){
            throw new UnprocessableDataException(
                    "Cannot sell more shares than you own");
        }

        BigDecimal stockPrice = stockService.getStockPrice(stockTicker);

        BigDecimal totalStocksPrice = stockPrice.multiply(BigDecimal.valueOf(quantity));

        BigDecimal feeAmount = calculateFeeAmount(totalStocksPrice);

        BigDecimal sellAmount = totalStocksPrice.subtract(feeAmount);

        Transaction savedTransaction = transactionService.save(
                user,
                TransactionType.STOCK_SELL,
                stock,
                quantity,
                stockPrice,
                sellAmount,
                feeAmount);

        balanceLedgerService.save(savedTransaction, BalanceLedgerType.CREDIT);

        positionLedgerService.save(savedTransaction, PositionLedgerType.SELL);

        userService.sumToBalance(user, sellAmount);

        positionService.stockSale(savedTransaction);

        return savedTransaction;
    }

    private BigDecimal calculateFeeAmount(BigDecimal grossValue){
        // B3 fee(0.03%) + IRRF(0.005%)
        BigDecimal B3Fee = new BigDecimal("0.0003");
        BigDecimal IRRFFee = new BigDecimal("0.00005");
        BigDecimal totalFee = B3Fee.add(IRRFFee);
        return grossValue.multiply(totalFee);
    }
}
