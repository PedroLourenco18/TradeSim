package br.com.pedrolourenco.TradeSim.service;

import br.com.pedrolourenco.TradeSim.domain.balance_ledger.BalanceLedger;
import br.com.pedrolourenco.TradeSim.domain.balance_ledger.BalanceLedgerType;
import br.com.pedrolourenco.TradeSim.domain.transaction.Transaction;
import br.com.pedrolourenco.TradeSim.domain.user.User;
import br.com.pedrolourenco.TradeSim.repository.BalanceLedgerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BalanceLedgerService {
    private final BalanceLedgerRepository balanceLedgerRepository;

    public void save(Transaction transaction, BalanceLedgerType type){
        BalanceLedger balanceLedger = new BalanceLedger();

        balanceLedger.setUser(transaction.getUser());
        balanceLedger.setTransaction(transaction);
        balanceLedger.setAmount(transaction.getAmount());
        balanceLedger.setType(type);

        balanceLedgerRepository.save(balanceLedger);
    }

    public BigDecimal calculateBalance(User user){
        return balanceLedgerRepository.findByUser(user).stream()
                .map(l -> l.getType() ==
                        BalanceLedgerType.CREDIT ? l.getAmount() : l.getAmount().negate())
                .reduce(BigDecimal::add)
                .orElseGet(() -> {
                    //TODO log de alerta
                    return BigDecimal.ZERO;
                });
    }
}
