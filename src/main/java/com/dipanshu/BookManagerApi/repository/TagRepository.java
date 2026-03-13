package com.dipanshu.BookManagerApi.repository;

import com.dipanshu.BookManagerApi.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag,Long> {

    // Find tag by name (used to reuse existing tags instead of creating duplicates)
    Optional<Tag> findByName(String name);
}