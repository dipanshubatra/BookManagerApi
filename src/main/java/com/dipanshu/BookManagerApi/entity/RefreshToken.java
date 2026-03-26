package com.dipanshu.BookManagerApi.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    @Column(nullable = false,unique = true)
    private String tokenHash;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private boolean revoked;
    @Column(nullable = false)
    private boolean used;
    private String deviceId;
    private Long parentId;
    @Column(nullable = false)
    private LocalDateTime expiryDate;
    @CreationTimestamp
    private LocalDateTime createdAt;
}






















