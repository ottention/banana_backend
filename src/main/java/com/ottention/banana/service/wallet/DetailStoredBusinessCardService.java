package com.ottention.banana.service.wallet;

import com.ottention.banana.dto.response.businesscard.StoredBusinessCardResponse;
import com.ottention.banana.entity.wallet.StoredBusinessCard;
import com.ottention.banana.repository.StoredBusinessCardRepository;
import com.ottention.banana.service.BusinessCardContentService;
import com.ottention.banana.service.ImageService;
import com.ottention.banana.service.TagService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DetailStoredBusinessCardService {
    private final StoredBusinessCardRepository storedCardRepository;
    private final BusinessCardContentService businessCardContentService;
    private final ImageService imageService;
    private final TagService tagService;

    public StoredBusinessCardResponse findStoredBusinessCard(Long id, Boolean isFront) {
        StoredBusinessCard storedBusinessCard = storedCardRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return toResponse(storedBusinessCard, isFront);
    }

    public StoredBusinessCardResponse toResponse(StoredBusinessCard storedCard, Boolean isFront) {
        return StoredBusinessCardResponse.builder()
                .id(storedCard.getId())
                .name(storedCard.getName())
                .contents(businessCardContentService.findByBusinessCardIdAndIsFront(storedCard.getBusinessCard().getId(), isFront))
                .images(imageService.findByBusinessCardIdAndIsFront(storedCard.getBusinessCard().getId(), isFront))
                .tags(tagService.getTags(storedCard.getBusinessCard().getId()))
                .build();
    }

    @Transactional
    public void delete(Long id) {
        storedCardRepository.deleteById(id);
    }
}
