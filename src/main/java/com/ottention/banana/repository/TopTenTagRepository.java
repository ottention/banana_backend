package com.ottention.banana.repository;

import com.ottention.banana.entity.TopTenTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopTenTagRepository extends JpaRepository<TopTenTag, Long> {
}
