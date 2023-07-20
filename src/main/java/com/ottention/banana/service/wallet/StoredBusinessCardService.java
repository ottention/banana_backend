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
        return toResponseList(storedCardRepository.findAllByUserId(id, pageable));
    }

    //즐겨찾기 최근 2개 조회
    public List<StoredCardPreviewResponse> getTwoBookmarkedCards(Long id) {
        return toResponseList(storedCardRepository.findTop2ByUserIdOrderByModifiedDateDesc(id));
    }

    //즐겨찾기 전체 조회
    public List<StoredCardPreviewResponse> getBookmarkedStoredCards(Long id, Pageable pageable) {
        return toResponseList(storedCardRepository.findAllByUserId(id, pageable).stream()
                .filter(StoredBusinessCard::getIsBookmarked)
                .toList());
    }

    //카테고리별 명함
    public List<StoredCardPreviewResponse> getStoredCardByCategory(Long categoryId, Pageable pageable) {
        return toResponseList(storedCardRepository.findAllByCategoryId(categoryId, pageable));
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
