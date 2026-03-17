package com.dipanshu.BookManagerApi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkResponse {
    private Long id;
    private String title;
    private String url;
    private String description;
    private LocalDateTime createdAt;
    private long visitCount;
    private boolean favorite;
}