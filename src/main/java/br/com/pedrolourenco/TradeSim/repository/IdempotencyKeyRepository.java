package br.com.pedrolourenco.TradeSim.repository;

import br.com.pedrolourenco.TradeSim.domain.idempotency_key.IdempotencyKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface IdempotencyKeyRepository extends JpaRepository<IdempotencyKey, UUID> {

    @Query(value = """
        SELECT * FROM idempotency_keys
        WHERE idempotency_key = :key
        AND path = :path
        AND user_id = :userId
        AND expire_at > :now
    """, nativeQuery = true)
    Optional<IdempotencyKey> findValid(
            @Param("key") UUID key,
            @Param("userId") UUID userId,
            @Param("path") String path,
            @Param("now") LocalDateTime now
    );

    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO idempotency_keys (idempotency_key, user_id, path, status, created_at, expire_at)
        VALUES (:key, :userId, :path, 'PROCESSING', NOW(), :expireAt)
        ON CONFLICT (idempotency_key, user_id, path) DO NOTHING
    """, nativeQuery = true)
    int insertIfAbsent(
            @Param("key") UUID key,
            @Param("userId") UUID userId,
            @Param("path") String path,
            @Param("expireAt") LocalDateTime expireAt
    );

    @Modifying
    @Transactional
    @Query(value = """
        UPDATE idempotency_keys
        SET status = 'COMPLETED',
            http_status = :httpStatus,
            response = :response
        WHERE idempotency_key = :key
        AND user_id = :userId
        AND path = :path
    """, nativeQuery = true)
    void markCompleted(
            @Param("key") UUID key,
            @Param("userId") UUID userId,
            @Param("path") String path,
            @Param("httpStatus") int httpStatus,
            @Param("response") String response
    );

    @Modifying
    @Transactional
    @Query(value = """
        DELETE FROM idempotency_keys
        WHERE expire_at < :now
        AND status = 'COMPLETED'
    """, nativeQuery = true)
    void deleteExpired(@Param("now") LocalDateTime now);
}
