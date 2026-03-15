package com.dipanshu.BookManagerApi.service;

import com.dipanshu.BookManagerApi.entity.Bookmark;
import com.dipanshu.BookManagerApi.repository.BookmarkRepository;
import com.dipanshu.BookManagerApi.repository.TagRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class BookmarkService {


    private final BookmarkRepository bookmarkRepository;
    private final TagRepository tagRepository;

    public BookmarkService(BookmarkRepository bookmarkRepository, TagRepository tagRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.tagRepository = tagRepository;
    }

    // Create bookmark and resolve tags
    @Transactional
    public Bookmark createBookmark(Bookmark bookmark) {

        if (bookmark.getTags() != null && !bookmark.getTags().isEmpty()) {

            List<String> tagNames = bookmark.getTags()
                    .stream()
                    .map(Tag::getName)
                    .toList();

            Set<Tag> resolvedTags = resolveTags(tagNames);
            bookmark.setTags(resolvedTags);
        }

        return bookmarkRepository.save(bookmark);
    }

    // Get all bookmarks
    @Transactional(readOnly = true)
    public List<Bookmark> findAllBookmarks() {
        return bookmarkRepository.findAll();
    }

    // Get bookmark by id
    @Transactional(readOnly = true)
    public Optional<Bookmark> findBookmarkById(Long id) {
        return bookmarkRepository.findById(id);
    }

    // Delete bookmark (soft delete handled by Hibernate)
    @Transactional
    public boolean deleteBookmarkById(Long id) {

        Optional<Bookmark> bookmarkOptional = bookmarkRepository.findById(id);

        if (bookmarkOptional.isPresent()) {
            bookmarkRepository.delete(bookmarkOptional.get());
            return true;
        }

        return false;
    }

    // Update bookmark
    @Transactional
    public Optional<Bookmark> updateBookmark(Long id, Bookmark updatedBookmark) {

        Optional<Bookmark> existingBookmark = bookmarkRepository.findById(id);

        if (existingBookmark.isPresent()) {

            Bookmark bookmark = existingBookmark.get();

            bookmark.setTitle(updatedBookmark.getTitle());
            bookmark.setUrl(updatedBookmark.getUrl());
            bookmark.setDescription(updatedBookmark.getDescription());

            if (updatedBookmark.getTags() != null && !updatedBookmark.getTags().isEmpty()) {

                List<String> tagNames = updatedBookmark.getTags()
                        .stream()
                        .map(Tag::getName)
                        .toList();

                bookmark.setTags(resolveTags(tagNames));
            }

            return Optional.of(bookmarkRepository.save(bookmark));
        }

        return Optional.empty();
    }

    // Pagination + sorting
    @Transactional(readOnly = true)
    public Page<Bookmark> findAllBookmarksPaginated(int page, int size, String sortBy, String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return bookmarkRepository.findAll(pageable);
    }

    // Search bookmarks
    @Transactional(readOnly = true)
    public Page<Bookmark> searchBookmarks(String query, int page, int size, String sortBy, String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return bookmarkRepository.searchBookmarks(query, pageable);
    }

    // Filter bookmarks by tag
    @Transactional(readOnly = true)
    public Page<Bookmark> findBookmarksByTag(String tagName, int page, int size, String sortBy, String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return bookmarkRepository.findByTagsName(tagName, pageable);
    }

    // Toggle favorite
    @Transactional
    public Optional<Bookmark> toggleFavorite(Long id) {

        Optional<Bookmark> bookmarkOptional = bookmarkRepository.findById(id);

        if (bookmarkOptional.isEmpty()) {
            return Optional.empty();
        }

        Bookmark bookmark = bookmarkOptional.get();

        bookmark.setFavorite(!bookmark.isFavorite());

        Bookmark updatedBookmark = bookmarkRepository.save(bookmark);

        return Optional.of(updatedBookmark);
    }

    // Record bookmark visit
    @Transactional
    public Optional<Bookmark> recordVisit(Long id) {

        Optional<Bookmark> bookmarkOptional = bookmarkRepository.findById(id);

        if (bookmarkOptional.isEmpty()) {
            return Optional.empty();
        }

        Bookmark bookmark = bookmarkOptional.get();

        bookmark.setVisitCount(bookmark.getVisitCount() + 1);
        bookmark.setLastVisitedAt(LocalDateTime.now());

        Bookmark updatedBookmark = bookmarkRepository.save(bookmark);

        return Optional.of(updatedBookmark);
    }

    // Resolve tag names
    private Set<Tag> resolveTags(List<String> tagNames) {

        Set<Tag> tags = new HashSet<>();

        for (String tagName : tagNames) {

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
