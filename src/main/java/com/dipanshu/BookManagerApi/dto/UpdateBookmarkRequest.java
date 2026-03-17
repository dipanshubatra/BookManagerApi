package com.dipanshu.BookManagerApi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBookmarkRequest {
    private String title;
    private String url;
    private String description;
}
