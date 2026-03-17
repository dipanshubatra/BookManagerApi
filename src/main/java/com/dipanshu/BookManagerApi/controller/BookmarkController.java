package com.dipanshu.BookManagerApi.controller;

import com.dipanshu.BookManagerApi.dto.*;
import com.dipanshu.BookManagerApi.service.BookmarkService;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    public BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<BookmarkResponse> createBookmark(
            @Valid @RequestBody CreateBookmarkRequest request) {

        return ResponseEntity.ok(bookmarkService.createBookmark(request));
    }

    // GET ALL (PAGINATED)
    @GetMapping
    public ResponseEntity<Page<BookmarkResponse>> getBookmarks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        return ResponseEntity.ok(
                bookmarkService.findAllBookmarksPaginated(page, size, sortBy, direction)
        );
    }

    // SEARCH
    @GetMapping("/search")
    public ResponseEntity<Page<BookmarkResponse>> searchBookmarks(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        return ResponseEntity.ok(
                bookmarkService.searchBookmarks(query, page, size, sortBy, direction)
        );
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<BookmarkResponse> getBookmark(@PathVariable Long id) {

        BookmarkResponse response = bookmarkService.findBookmarkById(id);
        return ResponseEntity.ok(response);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookmark(@PathVariable Long id) {

        bookmarkService.deleteBookmarkById(id);
        return ResponseEntity.noContent().build();
    }

    // UPDATE (PATCH)
    @PatchMapping("/{id}")
    public ResponseEntity<BookmarkResponse> updateBookmark(
            @PathVariable Long id,
            @Valid @RequestBody UpdateBookmarkRequest request) {

        BookmarkResponse updated = bookmarkService.updateBookmark(id, request);
        return ResponseEntity.ok(updated);
    }

    // FILTER BY TAG
    @GetMapping("/tags/{tagName}")
    public ResponseEntity<Page<BookmarkResponse>> getBookmarksByTag(
            @PathVariable String tagName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        return ResponseEntity.ok(
                bookmarkService.findBookmarksByTag(tagName, page, size, sortBy, direction)
        );
    }

    // TOGGLE FAVORITE
    @PatchMapping("/{id}/favorite")
    public ResponseEntity<BookmarkResponse> toggleFavorite(@PathVariable Long id) {

        return ResponseEntity.ok(bookmarkService.toggleFavorite(id));
    }

    // RECORD VISIT
    @PostMapping("/{id}/visit")
    public ResponseEntity<BookmarkResponse> recordVisit(@PathVariable Long id) {

        return ResponseEntity.ok(bookmarkService.recordVisit(id));
    }
}