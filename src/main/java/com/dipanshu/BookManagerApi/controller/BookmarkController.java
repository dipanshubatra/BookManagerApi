package com.dipanshu.BookManagerApi.controller;

import com.dipanshu.BookManagerApi.dto.*;
import com.dipanshu.BookManagerApi.service.BookmarkService;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    public BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    // CREATE
    @PostMapping
    public BookmarkResponse createBookmark(@Valid @RequestBody CreateBookmarkRequest request) {
        return bookmarkService.createBookmark(request);
    }

    // GET ALL (PAGINATED)
    @GetMapping
    public Page<BookmarkResponse> getBookmarks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        return bookmarkService.findAllBookmarksPaginated(page, size, sortBy, direction);
    }

    // SEARCH
    @GetMapping("/search")
    public Page<BookmarkResponse> searchBookmarks(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        return bookmarkService.searchBookmarks(query, page, size, sortBy, direction);
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<BookmarkResponse> getBookmark(@PathVariable Long id) {

        Optional<BookmarkResponse> bookmark = bookmarkService.findBookmarkById(id);

        return bookmark.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookmark(@PathVariable Long id) {

        boolean deleted = bookmarkService.deleteBookmarkById(id);

        return deleted
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    // UPDATE (PATCH behavior)
    @PatchMapping("/{id}")
    public ResponseEntity<BookmarkResponse> updateBookmark(
            @PathVariable Long id,
            @RequestBody UpdateBookmarkRequest request) {

        Optional<BookmarkResponse> updated =
                bookmarkService.updateBookmark(id, request);

        return updated.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // FILTER BY TAG
    @GetMapping("/tags/{tagName}")
    public Page<BookmarkResponse> getBookmarksByTag(
            @PathVariable String tagName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        return bookmarkService.findBookmarksByTag(tagName, page, size, sortBy, direction);
    }

    // TOGGLE FAVORITE
    @PatchMapping("/{id}/favorite")
    public ResponseEntity<BookmarkResponse> toggleFavorite(@PathVariable Long id) {

        return bookmarkService.toggleFavorite(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // RECORD VISIT
    @PostMapping("/{id}/visit")
    public ResponseEntity<BookmarkResponse> recordVisit(@PathVariable Long id) {

        return bookmarkService.recordVisit(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}