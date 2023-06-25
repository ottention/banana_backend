package com.ottention.banana.service.wallet;

import com.ottention.banana.dto.response.businesscard.StoredCardContentResponse;
import com.ottention.banana.dto.response.businesscard.StoredCardResponse;
import com.ottention.banana.entity.BusinessCardContent;
import com.ottention.banana.entity.StoredBusinessCard;
import com.ottention.banana.repository.BusinessCardContentRepository;
import com.ottention.banana.repository.StoredBusinessCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StoredBusinessCardService {

    static final boolean IS_FRONT = true;

    private final StoredBusinessCardRepository storedCardRepository;
    private final BusinessCardContentRepository businessCardContentRepository;


    //저장된 명함 조회
    public List<StoredCardResponse> getAllStoredBusinessCards(Long id, Pageable pageable) {
        List<StoredBusinessCard> storedBusinessCards = storedCardRepository.findAllByUserId(id, pageable);
        return getRequestedStoredCards(storedBusinessCards);
    }

    //즐겨찾기 최근 2개 조회
    public List<StoredCardResponse> getTwoBookmarkedCards(Long id) {
        List<StoredBusinessCard> storedBusinessCards = storedCardRepository.findTop2ByUserIdOrderByModifiedDateDesc(id);
        return getRequestedStoredCards(storedBusinessCards);
    }

    //즐겨찾기 전체 조회
    public List<StoredCardResponse> getBookmarkedStoredCards(Long id, Pageable pageable) {
        List<StoredBusinessCard> storedBusinessCards = storedCardRepository.findAllByUserIdAndIsBookmarkedTrue(id, pageable);
        return getRequestedStoredCards(storedBusinessCards);
    }

    //카테고리별 명함
    public List<StoredCardResponse> getStoredCardByCategory(long categoryId, Pageable pageable) {
        List<StoredBusinessCard> storedBusinessCards = storedCardRepository.findAllByCategoryId(categoryId, pageable);
        return getRequestedStoredCards(storedBusinessCards);
    }

    private List<StoredCardResponse> getRequestedStoredCards(List<StoredBusinessCard> storedBusinessCards) {
        List<StoredCardResponse> storedCardResponses = new ArrayList<>();
        storedBusinessCards.forEach(c -> storedCardResponses.add(new StoredCardResponse(c, getByBusinessCardId(c.getBusinessCard().getId(), IS_FRONT))));
        return storedCardResponses;
    }

    private List<StoredCardContentResponse> getByBusinessCardId(Long id, boolean isFront) {
        List<BusinessCardContent> businessCardContents = businessCardContentRepository.findByBusinessCardIdAndIsFront(id, isFront);
        List<StoredCardContentResponse> storedCardContentResponses = new ArrayList<>();
        businessCardContents.forEach(c -> storedCardContentResponses.add(new StoredCardContentResponse(c)));
        return storedCardContentResponses;
    }



}
