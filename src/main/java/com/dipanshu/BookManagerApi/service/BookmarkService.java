package com.dipanshu.BookManagerApi.service;

import com.dipanshu.BookManagerApi.entity.Bookmark;
import com.dipanshu.BookManagerApi.repository.BookmarkRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    public BookmarkService(BookmarkRepository bookmarkRepository){
        this.bookmarkRepository= bookmarkRepository;
    }
    public Bookmark createBookmark(Bookmark bookmark){
        return bookmarkRepository.save(bookmark);
    }
    public List<Bookmark> findAllBookmarks(){
        return bookmarkRepository.findAll();
    }


    public Optional<Bookmark> findBookmarkById(Long id) {
        Optional<Bookmark> bookmark = bookmarkRepository.findById(id);
        return bookmark;
    }
}
