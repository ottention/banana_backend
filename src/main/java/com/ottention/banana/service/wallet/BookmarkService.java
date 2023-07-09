package com.ottention.banana.service.wallet;

import com.ottention.banana.entity.wallet.StoredBusinessCard;
import com.ottention.banana.repository.wallet.StoredBusinessCardRepository;
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
    public void modifyBookmark(Long storedCardId, Boolean isBookmarked) {
        StoredBusinessCard target = repository.findById(storedCardId).orElseThrow(EntityNotFoundException::new);
        target.modifyIsBookmarked(isBookmarked);
    }
}
