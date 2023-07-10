package com.ottention.banana.service.wallet;

import com.ottention.banana.dto.response.businesscard.BackBusinessCardResponse;
import com.ottention.banana.dto.response.businesscard.FrontBusinessCardResponse;
import com.ottention.banana.dto.response.businesscard.wallet.StoredCardDetailResponse;
import com.ottention.banana.entity.BusinessCard;
import com.ottention.banana.entity.wallet.StoredBusinessCard;
import com.ottention.banana.repository.wallet.StoredBusinessCardRepository;
import com.ottention.banana.service.BusinessCardContentService;
import com.ottention.banana.service.ImageService;
import com.ottention.banana.service.LinkService;
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
    private final LinkService linkService;
    private final TagService tagService;

    public StoredCardDetailResponse findStoredCardDetail(Long id) {
        StoredBusinessCard storedBusinessCard = storedCardRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return toResponse(storedBusinessCard);
    }

    public StoredCardDetailResponse toResponse(StoredBusinessCard storedCard) {
        return StoredCardDetailResponse.builder()
                .id(storedCard.getId())
                .name(storedCard.getName())
                .front(findFrontStoredCardDetail(storedCard.getBusinessCard()))
                .back(findBackStoredCardDetail(storedCard.getBusinessCard()))
                .tags(tagService.getTags(storedCard.getBusinessCard().getId()))
                .build();
    }

    public FrontBusinessCardResponse findFrontStoredCardDetail(BusinessCard card) {
        return FrontBusinessCardResponse.builder()
                .frontContents(businessCardContentService.getFrontContents(card.getId()))
                .frontImages(imageService.getFrontImages(card.getId()))
                .frontLinks(linkService.getFrontLinks(card.getId()))
                .frontTemplateColor(card.getFrontTemplateColor())
                .build();
    }

    private BackBusinessCardResponse findBackStoredCardDetail(BusinessCard card) {
        return BackBusinessCardResponse.builder()
                .backContents(businessCardContentService.getBackContents(card.getId()))
                .backImages(imageService.getBackImages(card.getId()))
                .backLinks(linkService.getBackLinks(card.getId()))
                .backTemplateColor(card.getBackTemplateColor())
                .build();
    }



    @Transactional
    public void delete(Long id) {
        storedCardRepository.deleteById(id);
    }
}
