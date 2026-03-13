package com.dipanshu.BookManagerApi.service;

import com.dipanshu.BookManagerApi.entity.Bookmark;
import com.dipanshu.BookManagerApi.entity.Tag;
import com.dipanshu.BookManagerApi.repository.BookmarkRepository;
import com.dipanshu.BookManagerApi.repository.TagRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    // Create bookmark and resolve tags (reuse existing or create new)
    @Transactional
    public Bookmark createBookmark(Bookmark bookmark){
        if(bookmark.getTags() != null && !bookmark.getTags().isEmpty()){
            List<String> tagNames = bookmark.getTags().stream().map(Tag::getName).toList();
            Set<Tag> resolvedTags = resolveTags(tagNames);
            bookmark.setTags(resolvedTags);
        }
        return bookmarkRepository.save(bookmark);
    }

    // Fetch all bookmarks
    @Transactional(readOnly = true)
    public List<Bookmark> findAllBookmarks(){
        return bookmarkRepository.findAll();
    }

    // Fetch bookmark by ID
    @Transactional(readOnly = true)
    public Optional<Bookmark> findBookmarkById(Long id) {
        return bookmarkRepository.findById(id);
    }

    // Fetch bookmarks by tag name
    @Transactional(readOnly = true)
    public List<Bookmark> findBookmarksByTag(String tagName) {
        return bookmarkRepository.findByTagsName(tagName);
    }

    // Delete bookmark if it exists
    @Transactional
    public boolean deleteBookmarkById(Long id) {
        if(bookmarkRepository.existsById(id)){
            bookmarkRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Update bookmark details and tags
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

    // Pagination + sorting for bookmarks
    @Transactional(readOnly = true)
    public Page<Bookmark> findAllBookmarksPaginated(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return bookmarkRepository.findAll(pageable);
    }

    // Search bookmarks with pagination + sorting
    @Transactional(readOnly = true)
    public Page<Bookmark> searchBookmarks(String query, int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return bookmarkRepository.searchBookmarks(query, pageable);
    }

    // Resolve tag names: reuse existing tags or create new ones
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