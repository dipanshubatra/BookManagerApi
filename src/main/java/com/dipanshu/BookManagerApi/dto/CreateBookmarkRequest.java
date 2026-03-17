package com.dipanshu.BookManagerApi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookmarkRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String url;

    private String description;
}