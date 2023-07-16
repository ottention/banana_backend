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
import org.springframework.data.domain.PageRequest;
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
    @Scheduled(cron = "0 0 0 ? * MON")
    public void generateRandomTopTenTags() {
        List<String> tags = tagRepository.findTagsWithDuplicateCounts();
        log.info("tagsSize = {}", tags.size());
        Collections.shuffle(tags);
        List<String> randomTopTenTags = tags.subList(0, Math.min(10, tags.size()));

        topTenTagRepository.deleteAll();

        for (String tag : randomTopTenTags) {
            log.info("tag = {}", tag);
            TopTenTag topTenTag = new TopTenTag();
            topTenTag.updateTopTenTag(tag);
            topTenTagRepository.save(topTenTag);
        }
    }

    public List<BusinessCardResponse> getTopTenBusinessCards(String name, Long userId) {
        List<BusinessCardResponse> businessCardResponses = new ArrayList<>();

        PageRequest pageRequest = PageRequest.of(0, 10);
        List<BusinessCard> topTenBusinessCards = businessCardRepository.findTop10ByTagNameOrderByLikeCountDesc(name, pageRequest);
        log.info("topTenBusinessCards.size() = {}", topTenBusinessCards.size());

        for (BusinessCard topTenBusinessCard : topTenBusinessCards) {
            BusinessCardResponse businessCard = businessCardService.getBusinessCard(userId, topTenBusinessCard.getId());
            businessCardResponses.add(businessCard);
        }

        return businessCardResponses;
    }

}