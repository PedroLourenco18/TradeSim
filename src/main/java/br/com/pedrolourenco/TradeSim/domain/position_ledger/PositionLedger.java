package br.com.pedrolourenco.TradeSim.domain.position_ledger;

import br.com.pedrolourenco.TradeSim.domain.stock.Stock;
import br.com.pedrolourenco.TradeSim.domain.transaction.Transaction;
import br.com.pedrolourenco.TradeSim.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "positions_ledger")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class PositionLedger {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    @ManyToOne
    @JoinColumn(name = "asset_id")
    private Stock stock;

    private Long quantity;

    private PositionLedgerType type;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;
}
