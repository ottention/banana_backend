package com.ottention.banana.repository;

import com.ottention.banana.entity.StoredBusinessCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoredBusinessCardRepository extends JpaRepository<StoredBusinessCard, Long> {
    List<StoredBusinessCard> findByUserId(Long userId);
}

