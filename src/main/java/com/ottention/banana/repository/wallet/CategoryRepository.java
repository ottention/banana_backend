package com.ottention.banana.repository;

import com.ottention.banana.entity.wallet.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByUserIdOrderByModifiedDateDesc(Long userId);
}
