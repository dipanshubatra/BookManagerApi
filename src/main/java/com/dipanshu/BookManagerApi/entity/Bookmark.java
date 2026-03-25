package com.dipanshu.BookManagerApi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "bookmarks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String url;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "bookmark_tags",
            joinColumns = @JoinColumn(name = "bookmark_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"bookmark_id", "tag_id"})
    )
    private Set<Tag> tags = new HashSet<>();


    public void addTag(Tag tag) {
        this.tags.add(tag);
        tag.getBookmarks().add(this);
    }

    public void removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.getBookmarks().remove(this);
    }
}