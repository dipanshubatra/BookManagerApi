package com.dipanshu.BookManagerApi.repository;

import com.dipanshu.BookManagerApi.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark,Long> {
    @Query("SELECT DISTINCT b FROM Bookmark b JOIN FETCH b.tags t WHERE t.name = :tagName")
    List<Bookmark> findByTagsName(@Param("tagName") String tagName);
}