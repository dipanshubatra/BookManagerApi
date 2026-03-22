package com.dipanshu.BookManagerApi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class BookmarkRequestDTO {

    @NotBlank(message = "Title cannot be empty")
    @Size(max = 200)
    private String title;

    @NotBlank(message = "URL cannot be empty")
    private String url;

    @Size(max = 500)
    private String description;

    // Only tag names, not full Tag object
    private Set<String> tags;
}