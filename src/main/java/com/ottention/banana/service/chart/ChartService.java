package com.ottention.banana.service.chart;

import com.ottention.banana.dto.response.businesscard.BusinessCardResponse;
import com.ottention.banana.dto.response.businesscard.TagResponse;
import com.ottention.banana.entity.BusinessCard;
import com.ottention.banana.entity.TopTenTag;
import com.ottention.banana.repository.BusinessCardRepository;
import com.ottention.banana.repository.TopTenTagRepository;
import com.ottention.banana.service.BusinessCardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChartService {

    private final TopTenTagRepository topTenTagRepository;
    private final BusinessCardRepository businessCardRepository;
    private final BusinessCardService businessCardService;

    public List<TagResponse> getTopTenTags() {
        List<TopTenTag> topTenTags = topTenTagRepository.findAll();
        log.info("topTenTagsSize = {}", topTenTags.size());
        return topTenTags.stream().map(t -> new TagResponse(t.getTopTenTag()))
                .collect(toList());
    }

    public List<BusinessCardResponse> getTopTenBusinessCards(String name, Long userId, Pageable pageable) {
        //공개 명함만 데이터 조회
        List<BusinessCard> topTenBusinessCards = businessCardRepository.findTop10ByTagNameOrderByLikeCountDesc(name, pageable);
        log.info("topTenBusinessCards.size() = {}", topTenBusinessCards.size());

        return topTenBusinessCards.stream()
                .map(topTenBusinessCard -> businessCardService.getBusinessCard(userId, topTenBusinessCard.getId()))
                .collect(Collectors.toList());
    }

}