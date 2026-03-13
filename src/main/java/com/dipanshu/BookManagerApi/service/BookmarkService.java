package com.dipanshu.BookManagerApi.service;

import com.dipanshu.BookManagerApi.entity.Bookmark;
import com.dipanshu.BookManagerApi.entity.Tag;
import com.dipanshu.BookManagerApi.repository.BookmarkRepository;
import com.dipanshu.BookManagerApi.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final TagRepository tagRepository;

    public BookmarkService(BookmarkRepository bookmarkRepository, TagRepository tagRepository){
        this.bookmarkRepository = bookmarkRepository;
        this.tagRepository = tagRepository;
    }

    @Transactional
    public Bookmark createBookmark(Bookmark bookmark){
        if(bookmark.getTags() != null && !bookmark.getTags().isEmpty()){
            List<String> tagNames = bookmark.getTags().stream().map(Tag::getName).toList();
            Set<Tag> resolvedTags = resolveTags(tagNames);
            bookmark.setTags(resolvedTags);
        }
        return bookmarkRepository.save(bookmark);
    }

    @Transactional(readOnly = true)
    public List<Bookmark> findAllBookmarks(){
        return bookmarkRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Bookmark> findBookmarkById(Long id) {
        return bookmarkRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Bookmark> findBookmarksByTag(String tagName) {
        return bookmarkRepository.findByTagsName(tagName);
    }

    @Transactional
    public boolean deleteBookmarkById(Long id) {
        if(bookmarkRepository.existsById(id)){
            bookmarkRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    public Optional<Bookmark> updateBookmark(Long id, Bookmark updatedBookmark) {
        Optional<Bookmark> existingBookmark = bookmarkRepository.findById(id);

        if(existingBookmark.isPresent()){
            Bookmark bookmark = existingBookmark.get();

            bookmark.setTitle(updatedBookmark.getTitle());
            bookmark.setUrl(updatedBookmark.getUrl());
            bookmark.setDescription(updatedBookmark.getDescription());

            if(updatedBookmark.getTags() != null && !updatedBookmark.getTags().isEmpty()){
                List<String> tagNames = updatedBookmark.getTags().stream().map(Tag::getName).toList();
                bookmark.setTags(resolveTags(tagNames));
            }

            return Optional.of(bookmarkRepository.save(bookmark));
        }

        return Optional.empty();
    }

    private Set<Tag> resolveTags(List<String> tagNames){
        Set<Tag> tags = new HashSet<>();
        for(String tagName : tagNames){
            Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> {
                        Tag newTag = new Tag();
                        newTag.setName(tagName);
                        return tagRepository.save(newTag);
                    });
            tags.add(tag);
        }
        return tags;
    }
}