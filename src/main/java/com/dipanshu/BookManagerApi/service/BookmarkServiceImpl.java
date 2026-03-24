package com.dipanshu.BookManagerApi.service;

import com.dipanshu.BookManagerApi.dto.BookmarkRequestDTO;
import com.dipanshu.BookManagerApi.dto.BookmarkResponseDTO;
import com.dipanshu.BookManagerApi.entity.Bookmark;
import com.dipanshu.BookManagerApi.entity.Tag;
import com.dipanshu.BookManagerApi.exception.ResourceNotFoundException;
import com.dipanshu.BookManagerApi.mapper.BookmarkMapper;
import com.dipanshu.BookManagerApi.repository.BookmarkRepository;
import com.dipanshu.BookManagerApi.repository.TagRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class BookmarkServiceImpl implements BookmarkService {

    private static final Logger logger = LoggerFactory.getLogger(BookmarkServiceImpl.class);

    private final BookmarkRepository bookmarkRepository;
    private final TagRepository tagRepository;

    public BookmarkServiceImpl(BookmarkRepository bookmarkRepository, TagRepository tagRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    @Transactional
    public BookmarkResponseDTO createBookmark(BookmarkRequestDTO dto) {
        logger.info("Creating bookmark with title: {}", dto.getTitle());

        Bookmark bookmark = BookmarkMapper.toEntity(dto);

        if (dto.getTags() != null && !dto.getTags().isEmpty()) {
            List<String> tagNames = dto.getTags().stream()
                    .map(tag -> tag.trim().toLowerCase())
                    .toList();

            bookmark.setTags(resolveTags(tagNames));
        }

        Bookmark saved = bookmarkRepository.save(bookmark);
        return BookmarkMapper.toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookmarkResponseDTO> findAllBookmarks() {
        logger.info("Fetching all bookmarks");

        List<BookmarkResponseDTO> list = bookmarkRepository.findAll()
                .stream()
                .map(BookmarkMapper::toDTO)
                .toList();

        logger.info("Total bookmarks fetched: {}", list.size());
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public BookmarkResponseDTO findBookmarkById(Long id) {
        logger.info("Fetching bookmark with id: {}", id);

        return bookmarkRepository.findById(id)
                .map(BookmarkMapper::toDTO)
                .orElseThrow(() -> {
                    logger.warn("Bookmark not found with id: {}", id);
                    return new ResourceNotFoundException("Bookmark not found with id: " + id);
                });
    }

    @Override
    @Transactional
    public boolean deleteBookmarkById(Long id) {
        logger.info("Deleting bookmark with id: {}", id);

        Optional<Bookmark> bookmarkOptional = bookmarkRepository.findById(id);

        if (bookmarkOptional.isPresent()) {
            bookmarkRepository.delete(bookmarkOptional.get());
            return true;
        }

        logger.warn("Bookmark not found for deletion: {}", id);
        return false;
    }

    @Override
    @Transactional
    public Optional<BookmarkResponseDTO> updateBookmark(Long id, BookmarkRequestDTO dto) {
        logger.info("Updating bookmark with id: {}", id);

        Optional<Bookmark> existingBookmark = bookmarkRepository.findById(id);

        if (existingBookmark.isEmpty()) {
            logger.warn("Bookmark not found for update: {}", id);
            return Optional.empty();
        }

        Bookmark bookmark = existingBookmark.get();

        bookmark.setTitle(dto.getTitle());
        bookmark.setUrl(dto.getUrl());
        bookmark.setDescription(dto.getDescription());

        if (dto.getTags() != null && !dto.getTags().isEmpty()) {
            List<String> tagNames = dto.getTags().stream()
                    .map(tag -> tag.trim().toLowerCase())
                    .toList();

            bookmark.setTags(resolveTags(tagNames));
        }

        Bookmark updated = bookmarkRepository.save(bookmark);
        return Optional.of(BookmarkMapper.toDTO(updated));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookmarkResponseDTO> findAllBookmarksPaginated(int page, int size, String sortBy, String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return bookmarkRepository.findAll(pageable)
                .map(BookmarkMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookmarkResponseDTO> searchBookmarks(String query, int page, int size, String sortBy, String direction) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        return bookmarkRepository.searchBookmarks(query, pageable)
                .map(BookmarkMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookmarkResponseDTO> findBookmarksByTag(String tagName, int page, int size, String sortBy, String direction) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        return bookmarkRepository.findByTagsName(tagName, pageable)
                .map(BookmarkMapper::toDTO);
    }

    @Override
    @Transactional
    public Optional<BookmarkResponseDTO> toggleFavorite(Long id) {

        Optional<Bookmark> bookmarkOptional = bookmarkRepository.findById(id);

        if (bookmarkOptional.isEmpty()) return Optional.empty();

        Bookmark bookmark = bookmarkOptional.get();
        bookmark.setFavorite(!bookmark.isFavorite());

        Bookmark updated = bookmarkRepository.save(bookmark);
        return Optional.of(BookmarkMapper.toDTO(updated));
    }

    @Override
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