package com.ottention.banana.repository;

import com.ottention.banana.entity.GuestBook;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GuestBookRepository extends JpaRepository<GuestBook, Long> {
    List<GuestBook> findByUserId(Long userId, Pageable pageable);
    List<GuestBook> findByBusinessCardId(Long businessCardId, Pageable pageable);
}
