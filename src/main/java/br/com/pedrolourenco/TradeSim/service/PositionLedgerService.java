package br.com.pedrolourenco.TradeSim.service;

import br.com.pedrolourenco.TradeSim.domain.stock.Stock;
import br.com.pedrolourenco.TradeSim.domain.position_ledger.PositionLedger;
import br.com.pedrolourenco.TradeSim.domain.position_ledger.PositionLedgerType;
import br.com.pedrolourenco.TradeSim.domain.transaction.Transaction;
import br.com.pedrolourenco.TradeSim.domain.user.User;
import br.com.pedrolourenco.TradeSim.repository.PositionLedgerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PositionLedgerService {
    private final PositionLedgerRepository positionLedgerRepository;

    public void save(Transaction transaction, PositionLedgerType type){
        PositionLedger positionLedger = new PositionLedger();

        positionLedger.setUser(transaction.getUser());
        positionLedger.setStock(transaction.getStock());
        positionLedger.setQuantity(transaction.getQuantity());
        positionLedger.setTransaction(transaction);
        positionLedger.setType(type);

        positionLedgerRepository.save(positionLedger);
    }

    public Long calculateStockQuantity(User user, Stock stock){
        return positionLedgerRepository
                .findByUserAndStock(user, stock)
                .stream()
                .mapToLong(p -> p.getType() ==
                        PositionLedgerType.BUY? p.getQuantity() : -p.getQuantity())
                .sum();
    }
}
