package com.dipanshu.BookManagerApi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "tags")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tag {


    // Primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Tag name must be unique
    @Column(unique = true, nullable = false)
    private String name;

    // Inverse side of ManyToMany relationship
    @ManyToMany(mappedBy = "tags")
    @JsonIgnore
    private Set<Bookmark> bookmarks;

}
