package com.ottention.banana.service;

import com.ottention.banana.dto.response.businesscard.StoredCardContentResponse;
import com.ottention.banana.dto.response.businesscard.StoredCardResponse;
import com.ottention.banana.entity.BusinessCard;
import com.ottention.banana.entity.BusinessCardContent;
import com.ottention.banana.entity.StoredBusinessCard;
import com.ottention.banana.exception.BusinessCardNotFound;
import com.ottention.banana.repository.BusinessCardContentRepository;
import com.ottention.banana.repository.BusinessCardRepository;
import com.ottention.banana.repository.StoredBusinessCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WalletService {

    private final StoredBusinessCardRepository storedCardRepository;
    private final BusinessCardRepository businessCardRepository;
    private final BusinessCardContentRepository businessCardContentRepository;


    public List<StoredCardResponse> getList(Long id, boolean isFront) {
        List<StoredBusinessCard> storedBusinessCards = storedCardRepository.findByUserId(id);
        List<BusinessCard> businessCards = new ArrayList<>();
        List<StoredCardResponse> storedCardResponses = new ArrayList<>();

        storedBusinessCards.forEach(c -> businessCards.add(getBusinessCardByStoredCardId(c)));
        storedBusinessCards.forEach(c -> storedCardResponses.add(new StoredCardResponse(c, getByBusinessCardId(c.getBusinessCard().getId(), isFront))));

        return storedCardResponses;

    }


    private List<StoredCardContentResponse> getByBusinessCardId(Long id, boolean isFront) {
        List<BusinessCardContent> businessCardContents = businessCardContentRepository.findByBusinessCardIdAndIsFront(id, isFront);
        List<StoredCardContentResponse> storedCardContentResponses = new ArrayList<>();
        businessCardContents.forEach(c -> storedCardContentResponses.add(new StoredCardContentResponse(c)));
        return storedCardContentResponses;
    }

    private BusinessCard getBusinessCardByStoredCardId(StoredBusinessCard storedCard) {
        return businessCardRepository.findById(storedCard.getBusinessCard().getId())
                .orElseThrow(BusinessCardNotFound::new);
    }
}
