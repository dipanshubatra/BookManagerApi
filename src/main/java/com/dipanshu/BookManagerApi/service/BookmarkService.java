package com.dipanshu.BookManagerApi.service;

import com.dipanshu.BookManagerApi.dto.*;
import com.dipanshu.BookManagerApi.entity.Bookmark;
import com.dipanshu.BookManagerApi.entity.User;
import com.dipanshu.BookManagerApi.exception.ResourceNotFoundException;
import com.dipanshu.BookManagerApi.repository.BookmarkRepository;
import com.dipanshu.BookManagerApi.repository.UserRepository;

import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;

    public BookmarkService(BookmarkRepository bookmarkRepository,
                           UserRepository userRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.userRepository = userRepository;
    }

    // get logged-in user
    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    // convert entity → response DTO
    private BookmarkResponse mapToResponse(Bookmark b) {
        BookmarkResponse r = new BookmarkResponse();
        r.setId(b.getId());
        r.setTitle(b.getTitle());
        r.setUrl(b.getUrl());
        r.setDescription(b.getDescription());
        r.setCreatedAt(b.getCreatedAt());
        r.setVisitCount(b.getVisitCount());
        r.setFavorite(b.isFavorite());
        return r;
    }

    // CREATE
    @Transactional
    public BookmarkResponse createBookmark(CreateBookmarkRequest request) {

        User user = getCurrentUser();

        Bookmark bookmark = new Bookmark();
        bookmark.setTitle(request.getTitle());
        bookmark.setUrl(request.getUrl());
        bookmark.setDescription(request.getDescription());

        bookmark.setVisitCount(0);
        bookmark.setCreatedAt(LocalDateTime.now()); // set creation time
        bookmark.setUser(user);

        return mapToResponse(bookmarkRepository.save(bookmark));
    }

    // GET BY ID (user-scoped)
    @Transactional(readOnly = true)
    public BookmarkResponse findBookmarkById(Long id) {

        User user = getCurrentUser();

        Bookmark bookmark = bookmarkRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Bookmark not found"));

        return mapToResponse(bookmark);
    }

    // DELETE
    @Transactional
    public void deleteBookmarkById(Long id) {

        User user = getCurrentUser();

        Bookmark bookmark = bookmarkRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Bookmark not found"));

        bookmarkRepository.delete(bookmark);
    }

    // UPDATE (partial update)
    @Transactional
    public BookmarkResponse updateBookmark(Long id, UpdateBookmarkRequest request) {

        User user = getCurrentUser();

        Bookmark b = bookmarkRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Bookmark not found"));

        if (request.getTitle() != null)
            b.setTitle(request.getTitle());

        if (request.getUrl() != null)
            b.setUrl(request.getUrl());

        if (request.getDescription() != null)
            b.setDescription(request.getDescription());

        return mapToResponse(bookmarkRepository.save(b));
    }

    // PAGINATION
    @Transactional(readOnly = true)
    public Page<BookmarkResponse> findAllBookmarksPaginated(
            int page, int size, String sortBy, String direction) {

        User user = getCurrentUser();

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return bookmarkRepository.findByUser(user, pageable)
                .map(this::mapToResponse);
    }

    // SEARCH
    @Transactional(readOnly = true)
    public Page<BookmarkResponse> searchBookmarks(
            String query, int page, int size, String sortBy, String direction) {

        User user = getCurrentUser();

        Pageable pageable = PageRequest.of(page, size,
                direction.equalsIgnoreCase("desc")
                        ? Sort.by(sortBy).descending()
                        : Sort.by(sortBy).ascending());

        return bookmarkRepository.searchBookmarks(query, user, pageable)
                .map(this::mapToResponse);
    }

    // FILTER BY TAG
    @Transactional(readOnly = true)
    public Page<BookmarkResponse> findBookmarksByTag(
            String tagName, int page, int size, String sortBy, String direction) {

        User user = getCurrentUser();

        Pageable pageable = PageRequest.of(page, size,
                direction.equalsIgnoreCase("desc")
                        ? Sort.by(sortBy).descending()
                        : Sort.by(sortBy).ascending());

        return bookmarkRepository.findByUserAndTagsName(user, tagName, pageable)
                .map(this::mapToResponse);
    }

    // TOGGLE FAVORITE
    @Transactional
    public BookmarkResponse toggleFavorite(Long id) {

        User user = getCurrentUser();

        Bookmark b = bookmarkRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Bookmark not found"));

        b.setFavorite(!b.isFavorite()); // flip boolean

        return mapToResponse(bookmarkRepository.save(b));
    }

    // RECORD VISIT
    @Transactional
    public BookmarkResponse recordVisit(Long id) {

        User user = getCurrentUser();

        Bookmark b = bookmarkRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Bookmark not found"));

        b.setVisitCount(b.getVisitCount() + 1); // increment count
        b.setLastVisitedAt(LocalDateTime.now()); // update timestamp

        return mapToResponse(bookmarkRepository.save(b));
    }
}