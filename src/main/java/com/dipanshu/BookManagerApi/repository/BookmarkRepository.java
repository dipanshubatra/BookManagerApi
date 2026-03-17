package com.dipanshu.BookManagerApi.repository;

import com.dipanshu.BookManagerApi.entity.Bookmark;
import com.dipanshu.BookManagerApi.entity.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    // Enforce ownership (CRITICAL)
    Optional<Bookmark> findByIdAndUser(Long id, User user);

    // Pagination scoped to user
    Page<Bookmark> findByUser(User user, Pageable pageable);

    //  Filter by tag + user (optional improvement later)
    Page<Bookmark> findByUserAndTagsName(User user, String tagName, Pageable pageable);

    //  Search scoped to user
    @Query("SELECT b FROM Bookmark b WHERE b.user = :user AND (" +
            "LOWER(b.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(b.description) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Bookmark> searchBookmarks(@Param("query") String query,
                                   @Param("user") User user,
                                   Pageable pageable);
}