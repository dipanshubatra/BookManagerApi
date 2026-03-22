package com.dipanshu.BookManagerApi.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class BookmarkResponseDTO {

    private Long id;
    private String title;
    private String url;
    private String description;

    private boolean favorite;
    private long visitCount;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastVisitedAt;

    private Set<String> tags;
}