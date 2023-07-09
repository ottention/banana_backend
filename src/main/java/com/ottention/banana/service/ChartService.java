package com.ottention.banana.service;

import com.ottention.banana.dto.response.businesscard.BusinessCardResponse;
import com.ottention.banana.dto.response.businesscard.TagResponse;
import com.ottention.banana.entity.BusinessCard;
import com.ottention.banana.entity.TopTenTag;
import com.ottention.banana.repository.BusinessCardRepository;
import com.ottention.banana.repository.TagRepository;
import com.ottention.banana.repository.TopTenTagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChartService {

    private final TagRepository tagRepository;
    private final TopTenTagRepository topTenTagRepository;
    private final BusinessCardRepository businessCardRepository;
    private final BusinessCardService businessCardService;

    public List<TagResponse> getTopTenTags() {
        List<TopTenTag> topTenTags = topTenTagRepository.findAll();
        log.info("topTenTagsSize = {}", topTenTags.size());
        return topTenTags.stream().map(t -> new TagResponse(t.getTopTenTag()))
                .collect(toList());
    }

    @Transactional
    @Scheduled(fixedRate = 20000)
    public void generateRandomTop10Tags() {
        List<String> tags = tagRepository.findTagsWithDuplicateCounts();
        log.info("tagsSize = {}", tags.size());
        Collections.shuffle(tags);
        List<String> randomTop10Tags = tags.subList(0, Math.min(10, tags.size()));

        topTenTagRepository.deleteAll();

        for (String tag : randomTop10Tags) {
            log.info("tag = {}", tag);
            TopTenTag topTenTag = new TopTenTag();
            topTenTag.updateTopTenTag(tag);
            topTenTagRepository.save(topTenTag);
        }
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