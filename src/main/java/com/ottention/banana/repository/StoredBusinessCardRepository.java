package com.ottention.banana.repository;

import com.ottention.banana.entity.StoredBusinessCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoredBusinessCardRepository extends JpaRepository<StoredBusinessCard, Long> {
}

