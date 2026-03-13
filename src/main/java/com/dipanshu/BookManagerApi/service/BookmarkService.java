package com.dipanshu.BookManagerApi.service;

import com.dipanshu.BookManagerApi.entity.Bookmark;
import com.dipanshu.BookManagerApi.entity.Tag;
import com.dipanshu.BookManagerApi.repository.BookmarkRepository;
import com.dipanshu.BookManagerApi.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final TagRepository tagRepository;


    public BookmarkService(BookmarkRepository bookmarkRepository, TagRepository tagRepository){
        this.bookmarkRepository= bookmarkRepository;
        this.tagRepository = tagRepository;
    }
    public Bookmark createBookmark(Bookmark bookmark){
        List<String> tagNames = bookmark.getTags().stream().map(Tag::getName).toList();
        Set<Tag> resolvedTags = resolveTags(tagNames);
        bookmark.setTags(resolvedTags);
        
        return bookmarkRepository.save(bookmark);
    }
    public List<Bookmark> findAllBookmarks(){
        return bookmarkRepository.findAll();
    }


    public Optional<Bookmark> findBookmarkById(Long id) {
        Optional<Bookmark> bookmark = bookmarkRepository.findById(id);
        return bookmark;
    }

    public boolean deleteBookmarkById(Long id) {

        Optional<Bookmark> bookmark = bookmarkRepository.findById(id);

        if (bookmark.isPresent()) {
            bookmarkRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<Bookmark> updateBookmark(Long id, Bookmark updatedBookmark) {

        Optional<Bookmark> existingBookmark = bookmarkRepository.findById(id);

        if(existingBookmark.isPresent()){

            Bookmark bookmark = existingBookmark.get();

            bookmark.setTitle(updatedBookmark.getTitle());
            bookmark.setUrl(updatedBookmark.getUrl());
            bookmark.setDescription(updatedBookmark.getDescription());

            Bookmark savedBookmark = bookmarkRepository.save(bookmark);

            return Optional.of(savedBookmark);
        }

        return Optional.empty();
    }
    private Set<Tag> resolveTags(List<String> tagNames){
        Set<Tag>tags = new HashSet<>();
        for(String tagName :tagNames){
            Optional<Tag> existingTag = tagRepository.findByName(tagName);
            if(existingTag.isPresent()){
                tags.add(existingTag.get());
            }
            else{
                Tag newTag = new Tag();
                newTag.setName(tagName);
                Tag savedTag = tagRepository.save(newTag);
                tags.add(savedTag);
            }
        }
        return tags;
    }
}

