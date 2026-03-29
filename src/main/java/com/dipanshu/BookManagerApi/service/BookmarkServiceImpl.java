package com.dipanshu.BookManagerApi.service;
import org.springframework.security.core.context.SecurityContextHolder;
import com.dipanshu.BookManagerApi.entity.User;
import com.dipanshu.BookManagerApi.repository.UserRepository;
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
    private final UserRepository userRepository;

    public BookmarkServiceImpl(BookmarkRepository bookmarkRepository,
                               TagRepository tagRepository,
                               UserRepository userRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public BookmarkResponseDTO createBookmark(BookmarkRequestDTO dto) {

        long start = System.currentTimeMillis();
        logger.info("START createBookmark | title={} | url={}", dto.getTitle(), dto.getUrl());

        String username = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Bookmark bookmark = BookmarkMapper.toEntity(dto);
        bookmark.setUser(user); // 🔥 FIX

        if (dto.getTags() != null && !dto.getTags().isEmpty()) {
            List<String> tagNames = dto.getTags().stream()
                    .map(tag -> tag.trim().toLowerCase())
                    .toList();

            logger.debug("Resolving tags: {}", tagNames);
            bookmark.setTags(resolveTags(tagNames));
        }

        Bookmark saved = bookmarkRepository.save(bookmark);

        logger.info("SUCCESS createBookmark | id={} | timeTaken={}ms",
                saved.getId(), System.currentTimeMillis() - start);

        return BookmarkMapper.toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookmarkResponseDTO> findAllBookmarks() {

        List<BookmarkResponseDTO> list = bookmarkRepository.findAll()
                .stream()
                .map(BookmarkMapper::toDTO)
                .toList();

        logger.info("Fetched all bookmarks | count={}", list.size());

        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public BookmarkResponseDTO findBookmarkById(Long id) {

        logger.info("Fetching bookmark | id={}", id);

        Bookmark bookmark = bookmarkRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bookmark not found with id: " + id));

        return BookmarkMapper.toDTO(bookmark);
    }

    @Override
    @Transactional
    public boolean deleteBookmarkById(Long id) {

        long start = System.currentTimeMillis();
        logger.info("START deleteBookmarkById | id={}", id);

        Optional<Bookmark> optionalBookmark = bookmarkRepository.findById(id);

        if (optionalBookmark.isEmpty()) {
            logger.warn("NOT FOUND deleteBookmarkById | id={}", id);
            return false;
        }

        bookmarkRepository.delete(optionalBookmark.get());

        logger.info("SUCCESS deleteBookmarkById | id={} | timeTaken={}ms",
                id, System.currentTimeMillis() - start);

        return true;
    }

    @Override
    @Transactional
    public Optional<BookmarkResponseDTO> updateBookmark(Long id, BookmarkRequestDTO dto) {

        long start = System.currentTimeMillis();
        logger.info("START updateBookmark | id={}", id);

        Optional<Bookmark> optionalBookmark = bookmarkRepository.findById(id);

        if (optionalBookmark.isEmpty()) {
            logger.warn("NOT FOUND updateBookmark | id={}", id);
            return Optional.empty();
        }

        Bookmark bookmark = optionalBookmark.orElseThrow();

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

        logger.info("SUCCESS updateBookmark | id={} | timeTaken={}ms",
                id, System.currentTimeMillis() - start);

        return Optional.of(BookmarkMapper.toDTO(updated));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookmarkResponseDTO> findAllBookmarksPaginated(int page, int size, String sortBy, String direction) {

        long start = System.currentTimeMillis();
        logger.info("START findAllBookmarksPaginated | page={} | size={} | sortBy={} | direction={}",
                page, size, sortBy, direction);

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<BookmarkResponseDTO> result = bookmarkRepository.findAll(pageable)
                .map(BookmarkMapper::toDTO);

        logger.info("SUCCESS findAllBookmarksPaginated | totalElements={} | timeTaken={}ms",
                result.getTotalElements(), System.currentTimeMillis() - start);

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookmarkResponseDTO> searchBookmarks(String query, int page, int size, String sortBy, String direction) {

        long start = System.currentTimeMillis();
        logger.info("START searchBookmarks | query={} | page={} | size={} | sortBy={} | direction={}",
                query, page, size, sortBy, direction);

        Sort sort = "desc".equalsIgnoreCase(direction)
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<BookmarkResponseDTO> result = bookmarkRepository.searchBookmarks(query, pageable)
                .map(BookmarkMapper::toDTO);

        logger.info("SUCCESS searchBookmarks | results={} | timeTaken={}ms",
                result.getTotalElements(), System.currentTimeMillis() - start);

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookmarkResponseDTO> findBookmarksByTag(String tagName, int page, int size, String sortBy, String direction) {

        long start = System.currentTimeMillis();
        logger.info("START findBookmarksByTag | tag={} | page={} | size={} | sortBy={} | direction={}",
                tagName, page, size, sortBy, direction);

        Sort sort = "desc".equalsIgnoreCase(direction)
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<BookmarkResponseDTO> result = bookmarkRepository.findByTagsName(tagName, pageable)
                .map(BookmarkMapper::toDTO);

        logger.info("SUCCESS findBookmarksByTag | count={} | timeTaken={}ms",
                result.getTotalElements(), System.currentTimeMillis() - start);

        return result;
    }

    @Override
    @Transactional
    public Optional<BookmarkResponseDTO> toggleFavorite(Long id) {

        long start = System.currentTimeMillis();
        logger.info("START toggleFavorite | id={}", id);

        Optional<Bookmark> optional = bookmarkRepository.findById(id);

        if (optional.isEmpty()) {
            logger.warn("NOT FOUND toggleFavorite | id={}", id);
            return Optional.empty();
        }

        Bookmark bookmark = optional.orElseThrow();

        bookmark.setFavorite(!bookmark.isFavorite());

        Bookmark updated = bookmarkRepository.save(bookmark);

        logger.info("SUCCESS toggleFavorite | id={} | newValue={} | timeTaken={}ms",
                id, updated.isFavorite(), System.currentTimeMillis() - start);

        return Optional.of(BookmarkMapper.toDTO(updated));
    }

    @Override
    @Transactional
    public Optional<BookmarkResponseDTO> recordVisit(Long id) {

        long start = System.currentTimeMillis();
        logger.info("START recordVisit | id={}", id);

        Optional<Bookmark> optional = bookmarkRepository.findById(id);

        if (optional.isEmpty()) {
            logger.warn("NOT FOUND recordVisit | id={}", id);
            return Optional.empty();
        }

        Bookmark bookmark = optional.orElseThrow();

        bookmark.setVisitCount(bookmark.getVisitCount() + 1);
        bookmark.setLastVisitedAt(LocalDateTime.now());

        Bookmark updated = bookmarkRepository.save(bookmark);

        logger.info("SUCCESS recordVisit | id={} | visits={} | timeTaken={}ms",
                id, updated.getVisitCount(), System.currentTimeMillis() - start);

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