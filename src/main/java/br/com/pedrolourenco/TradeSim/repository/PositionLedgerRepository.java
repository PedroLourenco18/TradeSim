package br.com.pedrolourenco.TradeSim.repository;

import br.com.pedrolourenco.TradeSim.domain.stock.Stock;
import br.com.pedrolourenco.TradeSim.domain.position_ledger.PositionLedger;
import br.com.pedrolourenco.TradeSim.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PositionLedgerRepository extends JpaRepository<PositionLedger, UUID> {
    List<PositionLedger> findByUserAndStock(User user, Stock stock);
}
