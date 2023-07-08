package com.ottention.banana.repository;

import com.ottention.banana.entity.Link;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LinkRepository extends JpaRepository<Link, Long> {
    List<Link> findByBusinessCardIdAndIsFront(Long id, boolean isFront);
}
