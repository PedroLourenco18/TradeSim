package br.com.pedrolourenco.TradeSim.domain.idempotency_key;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "idempotency_keys")
@Getter
@Setter
@NoArgsConstructor
public class IdempotencyKey {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)

    @Column(name = "idempotency_key")
    private UUID key;

    @Column(name = "user_id")
    private UUID userId;

    private String path;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private RequestStatus status;

    @Column(name = "http_status")
    private Integer httpStatus;

    @Column(columnDefinition = "varchar(5000)")
    private String response;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "expire_at")
    private LocalDateTime expireAt;
}
