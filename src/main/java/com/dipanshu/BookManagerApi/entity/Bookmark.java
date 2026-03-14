package com.dipanshu.BookManagerApi.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "bookmarks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Bookmark {

    // Primary key (auto-increment)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Bookmark title
    private String title;

    // Bookmark URL
    private String url;

    // Optional description
    private String description;

    // Creation timestamp
    private LocalDateTime createdAt;

    // Last update timestamp
    private LocalDateTime updatedAt;

    // Many-to-many relationship with tags
    @ManyToMany
    @JoinTable(
            name = "bookmark_tags",
            joinColumns = @JoinColumn(name = "bookmark_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @JsonIgnoreProperties("bookmarks")
    private Set<Tag> tags;

    // Automatically set creation time before insert
    @PrePersist
    public void beforeSave() {
        createdAt = LocalDateTime.now();
    }

    // Automatically update timestamp before update
    @PreUpdate
    public void afterSave(){
        updatedAt = LocalDateTime.now();
    }

    @Column(nullable = false)
    private boolean deleted = false;
}