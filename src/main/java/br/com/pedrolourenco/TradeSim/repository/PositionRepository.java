package br.com.pedrolourenco.TradeSim.repository;

import br.com.pedrolourenco.TradeSim.domain.stock.Stock;
import br.com.pedrolourenco.TradeSim.domain.position.Position;
import br.com.pedrolourenco.TradeSim.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PositionRepository extends JpaRepository<Position, UUID> {
    Optional<Position> findByUserAndStock(User user, Stock stock);

    boolean existsByIdAndUserId(UUID id, UUID userId);

    List<Position> findByUser(User user);
}
