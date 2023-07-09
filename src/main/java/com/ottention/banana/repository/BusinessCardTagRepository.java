package com.ottention.banana.repository;

import com.ottention.banana.entity.BusinessCardTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BusinessCardTagRepository extends JpaRepository<BusinessCardTag, Long> {
    List<BusinessCardTag> findByBusinessCardId(Long businessCardId);

    List<BusinessCardTag> findByTagId(Long tagId);
}