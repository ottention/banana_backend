package com.ottention.banana.repository;

import com.ottention.banana.entity.StoredBusinessCard;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoredBusinessCardRepository extends JpaRepository<StoredBusinessCard, Long> {
    List<StoredBusinessCard> findAllByUserIdOrderByModifiedDateDesc(Long userId);
    List<StoredBusinessCard> findTop2ByUserIdOrderByModifiedDateDesc(Long userId);
    List<StoredBusinessCard> findAllByUserIdAndIsBookmarkedTrueOrderByModifiedDateDesc(Long userId, Pageable pageable);
}

