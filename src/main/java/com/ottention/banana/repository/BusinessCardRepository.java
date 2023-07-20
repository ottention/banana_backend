package com.ottention.banana.repository;

import com.ottention.banana.entity.BusinessCard;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BusinessCardRepository extends JpaRepository<BusinessCard, Long> {
    List<BusinessCard> findByUserId(Long userId);

    @Query("SELECT b FROM BusinessCard b JOIN b.businessCardTags bt JOIN bt.tag t WHERE t.name = :name AND b.isPublic = true ORDER BY b.likeCount DESC")
    List<BusinessCard> findTop10ByTagNameOrderByLikeCountDesc(@Param("name") String name, Pageable pageable);
}