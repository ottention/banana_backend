package com.ottention.banana.service;

import com.ottention.banana.entity.StoredBusinessCard;
import com.ottention.banana.repository.StoredBusinessCardRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookmarkService {

    private final StoredBusinessCardRepository repository;

    @Transactional
    public void modifyIsBookmarked(Long storedCardId) {
        StoredBusinessCard target = repository.findById(storedCardId).orElseThrow(EntityNotFoundException::new);
        target.modifyIsBookmarked(storedCardId, !target.getIsBookmarked());
    }
}
