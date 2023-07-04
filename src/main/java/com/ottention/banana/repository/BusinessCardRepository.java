package com.ottention.banana.repository;

import com.ottention.banana.entity.BusinessCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BusinessCardRepository extends JpaRepository<BusinessCard, Long> {
    List<BusinessCard> findByUserId(Long userId);
}