package com.dipanshu.BookManagerApi.controller;

import com.dipanshu.BookManagerApi.dto.BookmarkRequestDTO;
import com.dipanshu.BookManagerApi.dto.BookmarkResponseDTO;
import com.dipanshu.BookManagerApi.service.BookmarkServiceImpl;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/bookmarks")
public class BookmarkController {

    private final BookmarkServiceImpl bookmarkService;

    public BookmarkController(BookmarkServiceImpl bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @PostMapping
    public BookmarkResponseDTO createBookmark(
            @Valid @RequestBody BookmarkRequestDTO dto) {

        return bookmarkService.createBookmark(dto);
    }

    @GetMapping
    public Page<BookmarkResponseDTO> getBookmarks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        return bookmarkService.findAllBookmarksPaginated(page, size, sortBy, direction);
    }

    @GetMapping("/search")
    public Page<BookmarkResponseDTO> searchBookmarks(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        return bookmarkService.searchBookmarks(query, page, size, sortBy, direction);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookmarkResponseDTO> getBookmark(@PathVariable Long id) {

        BookmarkResponseDTO bookmark = bookmarkService.findBookmarkById(id);

        return ResponseEntity.ok(bookmark);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookmark(@PathVariable Long id) {

        boolean deleted = bookmarkService.deleteBookmarkById(id);

        return deleted
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookmarkResponseDTO> updateBookmark(
            @PathVariable Long id,
            @Valid @RequestBody BookmarkRequestDTO dto) {

        Optional<BookmarkResponseDTO> updated =
                bookmarkService.updateBookmark(id, dto);

        return updated.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/tags/{tagName}")
    public Page<BookmarkResponseDTO> getBookmarksByTag(
            @PathVariable String tagName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        return bookmarkService.findBookmarksByTag(tagName, page, size, sortBy, direction);
    }

    @PatchMapping("/{id}/favorite")
    public ResponseEntity<BookmarkResponseDTO> toggleFavorite(@PathVariable Long id) {

        Optional<BookmarkResponseDTO> bookmark =
                bookmarkService.toggleFavorite(id);

        return bookmark.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/visit")
    public ResponseEntity<BookmarkResponseDTO> recordVisit(@PathVariable Long id) {

        Optional<BookmarkResponseDTO> bookmark =
                bookmarkService.recordVisit(id);

        return bookmark.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}