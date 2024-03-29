package com.ottention.banana.repository.wallet;

import com.ottention.banana.entity.wallet.Note;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findAllByStoredBusinessCardId(Long storedBusinessCardId, Pageable pageable);
}
