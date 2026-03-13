package com.dipanshu.BookManagerApi.controller;

import com.dipanshu.BookManagerApi.entity.Bookmark;
import com.dipanshu.BookManagerApi.service.BookmarkService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/bookmarks")
public class BookmarkController {
    private final BookmarkService bookmarkService;


    public BookmarkController(BookmarkService bookmarkService){
        this.bookmarkService = bookmarkService;
    }


    @PostMapping
    public Bookmark createBookmark(@RequestBody Bookmark bookmark){
        return bookmarkService.createBookmark(bookmark);
    }


    @GetMapping
    public Page<Bookmark> getBookmarks(
            @RequestParam(defaultValue = "0")   int page,
            @RequestParam(defaultValue = "10")  int size,
            @RequestParam(defaultValue = "id")  String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        return bookmarkService.findAllBookmarksPaginated(page, size, sortBy, direction);
    }

    @GetMapping("/search")
    public Page<Bookmark> searchBookmarks(
            @RequestParam String query,
            @RequestParam(defaultValue = "0")   int page,
            @RequestParam(defaultValue = "10")  int size,
            @RequestParam(defaultValue = "id")  String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        return bookmarkService.searchBookmarks(query, page, size, sortBy, direction);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Bookmark> getBookmark(@PathVariable Long id){
    Optional<Bookmark> bookmark = bookmarkService.findBookmarkById(id);
    if(bookmark.isPresent()){
        return new ResponseEntity<>(bookmark.get(),HttpStatus.OK);
    }
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBookmark(@PathVariable Long id){

        boolean deleted = bookmarkService.deleteBookmarkById(id);

        if(deleted){
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/by-tag/{tagName}")
    public List<Bookmark> getBookmarksByTag(@PathVariable String tagName){
        return bookmarkService.findBookmarksByTag(tagName);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Bookmark> updateBookmark(
            @PathVariable Long id,
            @RequestBody Bookmark bookmark){

        Optional<Bookmark> updatedBookmark = bookmarkService.updateBookmark(id, bookmark);

        if(updatedBookmark.isPresent()){
            return new ResponseEntity<>(updatedBookmark.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}













