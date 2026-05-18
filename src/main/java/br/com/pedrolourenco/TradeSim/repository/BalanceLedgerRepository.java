package br.com.pedrolourenco.TradeSim.repository;

import br.com.pedrolourenco.TradeSim.domain.balance_ledger.BalanceLedger;
import br.com.pedrolourenco.TradeSim.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BalanceLedgerRepository extends JpaRepository<BalanceLedger, UUID> {
    List<BalanceLedger> findByUser(User user);
}
