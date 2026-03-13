package com.dipanshu.BookManagerApi.entity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Set;
@Entity
@Table(name = "bookmarks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String url;

    private String description;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @ManyToMany
    @JoinTable(
            name = "bookmark_tags",
            joinColumns = @JoinColumn(name = "bookmark_id"),
            inverseJoinColumns =   @JoinColumn(name = "tag_id")
    )
    @JsonIgnoreProperties("bookmarks")
    private Set<Tag> tags;
    @PrePersist
    public void beforeSave() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void afterSave(){
        updatedAt = LocalDateTime.now();
    }
}


