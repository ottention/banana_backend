package com.ottention.banana.repository;

import com.ottention.banana.entity.BusinessCardContent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BusinessCardContentRepository extends JpaRepository<BusinessCardContent, Long> {
    List<BusinessCardContent> findByBusinessCardIdAndIsFront(Long id, boolean isFront);
}