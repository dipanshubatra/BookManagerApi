package com.dipanshu.BookManagerApi.repository;

import com.dipanshu.BookManagerApi.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByTokenHash(String tokenHash);

    List<RefreshToken> findByUsernameAndRevokedFalse(String username);

    @Modifying
    @Query("update RefreshToken t set t.revoked = true where t.username = :username")
    void revokeAllByUsername(String username);
}