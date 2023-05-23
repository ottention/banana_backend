package com.ottention.banana.repository;

import com.ottention.banana.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByBusinessCardIdAndIsFront(Long id, boolean isFront);
}