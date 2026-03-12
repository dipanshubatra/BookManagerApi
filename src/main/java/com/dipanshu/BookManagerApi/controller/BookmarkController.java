package com.dipanshu.BookManagerApi.controller;

import com.dipanshu.BookManagerApi.entity.Bookmark;
import com.dipanshu.BookManagerApi.service.BookmarkService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
    public Bookmark getBookmark(@RequestBody int id){
        return
    }

}
