package com.dipanshu.BookManagerApi.controller;

import com.dipanshu.BookManagerApi.entity.Bookmark;
import com.dipanshu.BookManagerApi.service.BookmarkService;
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

    // Create bookmark
    @PostMapping
    public Bookmark createBookmark(@RequestBody Bookmark bookmark) {
        return bookmarkService.createBookmark(bookmark);
    }

    // Get bookmarks with pagination
    @GetMapping
    public Page<Bookmark> getBookmarks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        return bookmarkService.findAllBookmarksPaginated(page, size, sortBy, direction);
    }

    // Search bookmarks
    @GetMapping("/search")
    public Page<Bookmark> searchBookmarks(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        return bookmarkService.searchBookmarks(query, page, size, sortBy, direction);
    }

    // Get bookmark by id
    @GetMapping("/{id}")
    public ResponseEntity<Bookmark> getBookmark(@PathVariable Long id) {

        Optional<Bookmark> bookmark = bookmarkService.findBookmarkById(id);

        return bookmark.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete bookmark
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookmark(@PathVariable Long id) {

        boolean deleted = bookmarkService.deleteBookmarkById(id);

        if (deleted) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    // Update bookmark
    @PutMapping("/{id}")
    public ResponseEntity<Bookmark> updateBookmark(
            @PathVariable Long id,
            @RequestBody Bookmark bookmark) {

        Optional<Bookmark> updatedBookmark =
                bookmarkService.updateBookmark(id, bookmark);

        return updatedBookmark.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Filter by tag
    @GetMapping("/tags/{tagName}")
    public Page<Bookmark> getBookmarksByTag(
            @PathVariable String tagName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        return bookmarkService.findBookmarksByTag(tagName, page, size, sortBy, direction);
    }

    // Toggle favorite
    @PatchMapping("/{id}/favorite")
    public ResponseEntity<Bookmark> toggleFavorite(@PathVariable Long id) {

        Optional<Bookmark> bookmark =
                bookmarkService.toggleFavorite(id);

        return bookmark.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Record visit
    @PostMapping("/{id}/visit")
    public ResponseEntity<Bookmark> recordVisit(@PathVariable Long id) {

        Optional<Bookmark> bookmark =
                bookmarkService.recordVisit(id);

        return bookmark.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


}
