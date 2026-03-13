package com.dipanshu.BookManagerApi.repository;

import com.dipanshu.BookManagerApi.entity.Bookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark,Long> {

    // Fetch bookmarks associated with a specific tag (eagerly loads tags)
    @Query("SELECT DISTINCT b FROM Bookmark b JOIN FETCH b.tags t WHERE t.name = :tagName")
    List<Bookmark> findByTagsName(@Param("tagName") String tagName);

    // Fetch bookmarks with pagination
    Page<Bookmark> findAll(Pageable pageable);

    // Search bookmarks by title or description (case-insensitive)
    @Query("SELECT b FROM Bookmark b WHERE " +
            "LOWER(b.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(b.description) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Bookmark> searchBookmarks(@Param("query") String query, Pageable pageable);
}