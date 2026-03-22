package com.dipanshu.BookManagerApi.service;

import com.dipanshu.BookManagerApi.dto.BookmarkRequestDTO;
import com.dipanshu.BookManagerApi.dto.BookmarkResponseDTO;
import com.dipanshu.BookManagerApi.entity.Bookmark;
import com.dipanshu.BookManagerApi.entity.Tag;
import com.dipanshu.BookManagerApi.exception.ResourceNotFoundException;
import com.dipanshu.BookManagerApi.mapper.BookmarkMapper;
import com.dipanshu.BookManagerApi.repository.BookmarkRepository;
import com.dipanshu.BookManagerApi.repository.TagRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final TagRepository tagRepository;

    public BookmarkService(BookmarkRepository bookmarkRepository, TagRepository tagRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.tagRepository = tagRepository;
    }

    //  CREATE
    @Transactional
    public BookmarkResponseDTO createBookmark(BookmarkRequestDTO dto) {

        Bookmark bookmark = BookmarkMapper.toEntity(dto);

        if (dto.getTags() != null && !dto.getTags().isEmpty()) {

            List<String> tagNames = dto.getTags()
                    .stream()
                    .map(tag -> tag.trim().toLowerCase())
                    .toList();

            bookmark.setTags(resolveTags(tagNames));
        }

        Bookmark saved = bookmarkRepository.save(bookmark);

        return BookmarkMapper.toDTO(saved);
    }

    // GET ALL (LIST)
    @Transactional(readOnly = true)
    public List<BookmarkResponseDTO> findAllBookmarks() {

        return bookmarkRepository.findAll()
                .stream()
                .map(BookmarkMapper::toDTO)
                .toList();
    }

    //  GET BY ID
    @Transactional(readOnly = true)
    public BookmarkResponseDTO findBookmarkById(Long id) {

        return bookmarkRepository.findById(id)
                .map(BookmarkMapper::toDTO)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Bookmark not found with id: " + id)
                );
    }

    //  DELETE
    @Transactional
    public boolean deleteBookmarkById(Long id) {

        Optional<Bookmark> bookmarkOptional = bookmarkRepository.findById(id);

        if (bookmarkOptional.isPresent()) {
            bookmarkRepository.delete(bookmarkOptional.get());
            return true;
        }

        return false;
    }

    //  UPDATE
    @Transactional
    public Optional<BookmarkResponseDTO> updateBookmark(Long id, BookmarkRequestDTO dto) {

        Optional<Bookmark> existingBookmark = bookmarkRepository.findById(id);

        if (existingBookmark.isEmpty()) {
            return Optional.empty();
        }

        Bookmark bookmark = existingBookmark.get();

        bookmark.setTitle(dto.getTitle());
        bookmark.setUrl(dto.getUrl());
        bookmark.setDescription(dto.getDescription());

        if (dto.getTags() != null && !dto.getTags().isEmpty()) {

            List<String> tagNames = dto.getTags()
                    .stream()
                    .map(tag -> tag.trim().toLowerCase())
                    .toList();

            bookmark.setTags(resolveTags(tagNames));
        }

        Bookmark updated = bookmarkRepository.save(bookmark);

        return Optional.of(BookmarkMapper.toDTO(updated));
    }

    //  PAGINATION
    @Transactional(readOnly = true)
    public Page<BookmarkResponseDTO> findAllBookmarksPaginated(
            int page, int size, String sortBy, String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return bookmarkRepository.findAll(pageable)
                .map(BookmarkMapper::toDTO);
    }

    // SEARCH
    @Transactional(readOnly = true)
    public Page<BookmarkResponseDTO> searchBookmarks(
            String query, int page, int size, String sortBy, String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return bookmarkRepository.searchBookmarks(query, pageable)
                .map(BookmarkMapper::toDTO);
    }

    // FILTER BY TAG
    @Transactional(readOnly = true)
    public Page<BookmarkResponseDTO> findBookmarksByTag(
            String tagName, int page, int size, String sortBy, String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return bookmarkRepository.findByTagsName(tagName, pageable)
                .map(BookmarkMapper::toDTO);
    }

    // TOGGLE FAVORITE
    @Transactional
    public Optional<BookmarkResponseDTO> toggleFavorite(Long id) {

        Optional<Bookmark> bookmarkOptional = bookmarkRepository.findById(id);

        if (bookmarkOptional.isEmpty()) return Optional.empty();

        Bookmark bookmark = bookmarkOptional.get();
        bookmark.setFavorite(!bookmark.isFavorite());

        Bookmark updated = bookmarkRepository.save(bookmark);

        return Optional.of(BookmarkMapper.toDTO(updated));
    }

    // RECORD VISIT
    @Transactional
    public Optional<BookmarkResponseDTO> recordVisit(Long id) {

        Optional<Bookmark> bookmarkOptional = bookmarkRepository.findById(id);

        if (bookmarkOptional.isEmpty()) return Optional.empty();

        Bookmark bookmark = bookmarkOptional.get();

        bookmark.setVisitCount(bookmark.getVisitCount() + 1);
        bookmark.setLastVisitedAt(LocalDateTime.now());

        Bookmark updated = bookmarkRepository.save(bookmark);

        return Optional.of(BookmarkMapper.toDTO(updated));
    }

    // RESOLVE TAGS
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