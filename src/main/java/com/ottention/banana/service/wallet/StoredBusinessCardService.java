package com.ottention.banana.service.wallet;

import com.ottention.banana.dto.response.businesscard.wallet.StoredCardDetailResponse;
import com.ottention.banana.dto.response.businesscard.wallet.StoredCardPreviewResponse;
import com.ottention.banana.entity.wallet.StoredBusinessCard;
import com.ottention.banana.repository.wallet.StoredBusinessCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StoredBusinessCardService {

    private final DetailStoredBusinessCardService detailStoredCardService;
    private final StoredBusinessCardRepository storedCardRepository;


    //저장된 명함 조회
    public List<StoredCardPreviewResponse> getAllStoredBusinessCards(Long id, Pageable pageable) {
        List<StoredBusinessCard> storedBusinessCards = storedCardRepository.findAllByUserId(id, pageable);
        return toResponseList(storedBusinessCards);
    }

    //즐겨찾기 최근 2개 조회
    public List<StoredCardPreviewResponse> getTwoBookmarkedCards(Long id) {
        List<StoredBusinessCard> storedBusinessCards = storedCardRepository.findTop2ByUserIdOrderByModifiedDateDesc(id);
        return toResponseList(storedBusinessCards);
    }

    //즐겨찾기 전체 조회
    public List<StoredCardPreviewResponse> getBookmarkedStoredCards(Long id, Pageable pageable) {
        List<StoredBusinessCard> storedBusinessCards = storedCardRepository.findAllByUserIdAndIsBookmarkedTrue(id, pageable);
        return toResponseList(storedBusinessCards);
    }

    //카테고리별 명함
    public List<StoredCardPreviewResponse> getStoredCardByCategory(Long categoryId, Pageable pageable) {
        List<StoredBusinessCard> storedBusinessCards = storedCardRepository.findAllByCategoryId(categoryId, pageable);
        return toResponseList(storedBusinessCards);
    }

    private List<StoredCardPreviewResponse> toResponseList(List<StoredBusinessCard> storedCards) {
        return storedCards.stream().map(this::toResponse).toList();
    }

    private StoredCardPreviewResponse toResponse(StoredBusinessCard storedCard) {
        return StoredCardPreviewResponse.builder()
                .id(storedCard.getBusinessCard().getId())
                .name(storedCard.getName())
                .front(detailStoredCardService.findFrontStoredCardDetail(storedCard.getBusinessCard()))
                .build();
    }
}
