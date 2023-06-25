package com.ottention.banana.service.wallet;

import com.ottention.banana.dto.response.businesscard.BusinessCardResponse;
import com.ottention.banana.entity.StoredBusinessCard;
import com.ottention.banana.repository.StoredBusinessCardRepository;
import com.ottention.banana.service.BusinessCardService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DetailStoredBusinessCardService {
    private final StoredBusinessCardRepository storedCardRepository;
    private final BusinessCardService businessCardService;

    public BusinessCardResponse findStoredBusinessCardFront(Long id, Pageable pageable) {
        return businessCardService.getFrontBusinessCard(getBusinessCardId(id));
    }

    public BusinessCardResponse findStoredBusinessCardBack(Long id) {
        return businessCardService.getBackBusinessCard(getBusinessCardId(id));
    }

    public Long getBusinessCardId(Long id) {
        StoredBusinessCard storedBusinessCard = storedCardRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return storedBusinessCard.getBusinessCard().getId();
    }

}
