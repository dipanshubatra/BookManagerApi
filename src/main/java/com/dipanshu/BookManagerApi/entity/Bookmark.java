package com.dipanshu.BookManagerApi.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "bookmarks")
@SoftDelete(strategy = SoftDeleteType.DELETED)
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
    @NotBlank(message = "Title cannot be empty")
    @Size(max=200)
    private String title;

    // Bookmark URL
    @NotBlank(message = "Url cannot be empty")
    private String url;

    // Optional description
    @Size(max=500)
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
    @CreationTimestamp
    @PrePersist
    public void beforeSave() {
        createdAt = LocalDateTime.now();
    }

    // Automatically update timestamp before update
    @UpdateTimestamp
    @PreUpdate
    public void afterSave(){
        updatedAt = LocalDateTime.now();
    }

    @Column(nullable = false)
    private boolean favorite = false;

    @Column(nullable = false)
    private long visitCount = 0;

    private LocalDateTime lastVisitedAt;

}