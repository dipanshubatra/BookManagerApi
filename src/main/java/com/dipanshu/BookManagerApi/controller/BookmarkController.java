package com.dipanshu.BookManagerApi.controller;

import com.dipanshu.BookManagerApi.entity.Bookmark;
import com.dipanshu.BookManagerApi.service.BookmarkService;
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
    public List<Bookmark> getBookmarks(){
        return bookmarkService.findAllBookmarks();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Bookmark> getBookmark(@PathVariable Long id){
    Optional<Bookmark> bookmark = bookmarkService.findBookmarkById(id);
    if(bookmark.isPresent()){
        return new ResponseEntity<>(bookmark.get(),HttpStatus.OK);
    }
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
