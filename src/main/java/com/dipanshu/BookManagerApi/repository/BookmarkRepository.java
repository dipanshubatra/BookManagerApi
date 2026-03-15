package com.dipanshu.BookManagerApi.repository;

import com.dipanshu.BookManagerApi.entity.Bookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {


    // Find bookmarks by tag name with pagination
    Page<Bookmark> findByTagsName(String tagName, Pageable pageable);

    // Search bookmarks by title or description
    @Query("SELECT b FROM Bookmark b WHERE " +
            "LOWER(b.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(b.description) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Bookmark> searchBookmarks(@Param("query") String query, Pageable pageable);

}
