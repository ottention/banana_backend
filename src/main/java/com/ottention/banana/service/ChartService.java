package com.ottention.banana.service;

import com.ottention.banana.dto.response.businesscard.BusinessCardResponse;
import com.ottention.banana.dto.response.businesscard.TagResponse;
import com.ottention.banana.entity.BusinessCard;
import com.ottention.banana.repository.BusinessCardRepository;
import com.ottention.banana.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChartService {

    private final TagRepository tagRepository;
    private final BusinessCardRepository businessCardRepository;
    private final BusinessCardService businessCardService;

    public List<TagResponse> getTopTenTags() {
        List<String> topTenTags = tagRepository.findTop10Tags();
        log.info("topTenTagsSize = {}", topTenTags.size());
        return topTenTags.stream().map(t -> new TagResponse(t))
                .collect(toList());
    }

    public List<BusinessCardResponse> getTopTenBusinessCards(Long userId) {
        List<BusinessCardResponse> businessCardResponses = new ArrayList<>();
        List<BusinessCard> businessCards = businessCardRepository.findTop10ByIsPublicOrderByLikeCountDesc(true);
        log.info("businessCardsSize = {}", businessCards.size());

        for (BusinessCard businessCard : businessCards) {
            BusinessCardResponse businessCardResponse = businessCardService.getBusinessCard(userId, businessCard.getId());
            businessCardResponses.add(businessCardResponse);
        }

        return businessCardResponses;
    }

}