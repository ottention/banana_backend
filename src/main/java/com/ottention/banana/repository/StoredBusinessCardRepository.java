package com.ottention.banana.repository;

import com.ottention.banana.entity.StoredBusinessCard;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoredBusinessCardRepository extends JpaRepository<StoredBusinessCard, Long> {
    List<StoredBusinessCard> findAllByUserId(Long userId, Pageable pageable);
    List<StoredBusinessCard> findTop2ByUserIdOrderByModifiedDateDesc(Long userId);
    List<StoredBusinessCard> findAllByUserIdAndIsBookmarkedTrue(Long userId, Pageable pageable);
    List<StoredBusinessCard> findAllByCategoryId(Long categoryId, Pageable pageable);
}

