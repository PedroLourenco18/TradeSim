package br.com.pedrolourenco.TradeSim.repository;

import br.com.pedrolourenco.TradeSim.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByIdAndActiveIsTrue(UUID uuid);

    Optional<User> findByCpfAndActiveIsTrue(String cpf);

    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);

    boolean existsByIdAndActiveIsTrue(UUID id);

    boolean existsByEmailIsAndIdIsNot(String email, UUID id);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.nickname = :nickname, u.email = :email WHERE u.id = :id")
    void updateNicknameAndEmail(@Param("id") UUID id,
                                @Param("nickname") String nickname,
                                @Param("email") String email);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.active = false WHERE u.id = :id")
    void deactivate(@Param("id") UUID id);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.balance = u.balance + :amount WHERE u.id = :id")
    void addBalance(@Param("id") UUID id, @Param("amount") BigDecimal amount);
}
