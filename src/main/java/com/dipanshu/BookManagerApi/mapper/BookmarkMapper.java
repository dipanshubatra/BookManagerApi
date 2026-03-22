package com.dipanshu.BookManagerApi.mapper;

import com.dipanshu.BookManagerApi.dto.BookmarkRequestDTO;
import com.dipanshu.BookManagerApi.dto.BookmarkResponseDTO;
import com.dipanshu.BookManagerApi.entity.Bookmark;
import com.dipanshu.BookManagerApi.entity.Tag;

import java.util.Set;
import java.util.stream.Collectors;

public class BookmarkMapper {

    public static Bookmark toEntity(BookmarkRequestDTO dto) {
        Bookmark bookmark = new Bookmark();
        bookmark.setTitle(dto.getTitle());
        bookmark.setUrl(dto.getUrl());
        bookmark.setDescription(dto.getDescription());
        return bookmark;
    }

    public static BookmarkResponseDTO toDTO(Bookmark bookmark) {

        Set<String> tags = null;

        if (bookmark.getTags() != null) {
            tags = bookmark.getTags()
                    .stream()
                    .map(Tag::getName)
                    .collect(Collectors.toSet());
        }

        return BookmarkResponseDTO.builder()
                .id(bookmark.getId())
                .title(bookmark.getTitle())
                .url(bookmark.getUrl())
                .description(bookmark.getDescription())
                .favorite(bookmark.isFavorite())
                .visitCount(bookmark.getVisitCount())
                .createdAt(bookmark.getCreatedAt())
                .updatedAt(bookmark.getUpdatedAt())
                .lastVisitedAt(bookmark.getLastVisitedAt())
                .tags(tags)
                .build();
    }
}