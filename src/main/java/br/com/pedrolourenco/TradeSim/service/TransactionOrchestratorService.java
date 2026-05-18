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
    public void withdraw(User user, BigDecimal amount){
        if(amount.compareTo(balanceLedgerService.calculateBalance(user)) > 0){
            throw new UnprocessableDataException(
                    "Não é possível sacar um valor maior que seu saldo");
        }

        userService.sumToBalance(user, amount.negate());

        Transaction savedTransaction = transactionService.save(user, TransactionType.WITHDRAW, amount);

        balanceLedgerService.save(savedTransaction, BalanceLedgerType.DEBIT);
    }

    @Transactional
    public void deposit(User user, BigDecimal amount){
        userService.sumToBalance(user, amount);

        Transaction savedTransaction = transactionService.save(user, TransactionType.DEPOSIT, amount);

        balanceLedgerService.save(savedTransaction, BalanceLedgerType.CREDIT);
    }

    @Transactional
    public Transaction buyStock(User user, String stockTicker, Long quantity){
        Stock stock = stockService.findByTicker(stockTicker);

        BigDecimal stockPrice = stockService.getStockPrice(stockTicker);

        BigDecimal purchasePrice = stockPrice.multiply(BigDecimal.valueOf(quantity));

        if(purchasePrice.compareTo(balanceLedgerService.calculateBalance(user)) > 0){
            throw new UnprocessableDataException(
                    "Não é possível fazer uma compra com valor maior que o saldo");
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
                    "Não é possível vender mais ações do que voce possui");
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
        //taxa B3(0,03%) + IRRF(0,005%)
        BigDecimal B3Fee = new BigDecimal("0.0003");
        BigDecimal IRRFFee = new BigDecimal("0.00005");
        BigDecimal totalFee = B3Fee.add(IRRFFee);
        return grossValue.multiply(totalFee);
    }
}
