package com.ottention.banana.repository;

import com.ottention.banana.entity.BusinessCardLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BusinessCardLikeRepository extends JpaRepository<BusinessCardLike, Long> {
    Optional<BusinessCardLike> findByUserIdAndBusinessCardId(Long userId, Long businessCardId);
    Boolean existsByUserIdAndBusinessCardId(Long userId, Long businessCardId);
}
