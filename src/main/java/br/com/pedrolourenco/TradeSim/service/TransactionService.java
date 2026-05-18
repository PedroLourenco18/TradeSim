package br.com.pedrolourenco.TradeSim.service;

import br.com.pedrolourenco.TradeSim.domain.stock.Stock;
import br.com.pedrolourenco.TradeSim.domain.transaction.Transaction;
import br.com.pedrolourenco.TradeSim.domain.transaction.TransactionType;
import br.com.pedrolourenco.TradeSim.domain.user.User;
import br.com.pedrolourenco.TradeSim.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public Transaction save(User user, TransactionType type, Stock stock, Long quantity, BigDecimal price, BigDecimal amount, BigDecimal fee) {
        Transaction transaction = new Transaction();

        transaction.setUser(user);
        transaction.setStock(stock);
        transaction.setPrice(price);
        transaction.setQuantity(quantity);
        transaction.setAmount(amount);
        transaction.setFee(fee);
        transaction.setType(type);

        return transactionRepository.save(transaction);
    }

    public Transaction save(User user, TransactionType type, BigDecimal amount){
        return save(user, type, null, null, null, amount, BigDecimal.ZERO);
    }

    public Page<Transaction> list(UUID userId, Pageable pageable, LocalDate startDate, LocalDate endDate){
        return transactionRepository.findAllByUserIdAndCreatedAtBetween(
                userId,
                startDate.atStartOfDay(),
                endDate.plusDays(1).atStartOfDay(),
                pageable);
    }
}
