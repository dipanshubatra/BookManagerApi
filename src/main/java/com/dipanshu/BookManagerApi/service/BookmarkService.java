package com.dipanshu.BookManagerApi.service;

import com.dipanshu.BookManagerApi.dto.BookmarkRequestDTO;
import com.dipanshu.BookManagerApi.dto.BookmarkResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface BookmarkService {

    BookmarkResponseDTO createBookmark(BookmarkRequestDTO dto);

    List<BookmarkResponseDTO> findAllBookmarks();

    BookmarkResponseDTO findBookmarkById(Long id);

    boolean deleteBookmarkById(Long id);

    Optional<BookmarkResponseDTO> updateBookmark(Long id, BookmarkRequestDTO dto);

    Page<BookmarkResponseDTO> findAllBookmarksPaginated(
            int page, int size, String sortBy, String direction);

    Page<BookmarkResponseDTO> searchBookmarks(
            String query, int page, int size, String sortBy, String direction);

    Page<BookmarkResponseDTO> findBookmarksByTag(
            String tagName, int page, int size, String sortBy, String direction);

    Optional<BookmarkResponseDTO> toggleFavorite(Long id);

    Optional<BookmarkResponseDTO> recordVisit(Long id);
}