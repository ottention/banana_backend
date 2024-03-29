package com.ottention.banana.repository;

import com.ottention.banana.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
    @Query("SELECT t.name FROM Tag t GROUP BY t.name HAVING COUNT(t.name) >= 10")
    List<String> findTagsWithDuplicateCounts();
}
