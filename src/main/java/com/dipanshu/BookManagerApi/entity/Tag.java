package com.dipanshu.BookManagerApi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tags")
public class Tag {

    // Primary key (auto-increment)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Unique tag name (cannot be null)
    @Column(unique = true, nullable = false)
    private String name;

    // Many-to-many relationship with bookmarks (inverse side)
    @ManyToMany(mappedBy = "tags")
    @JsonIgnore
    private Set<Bookmark> bookmarks;
}