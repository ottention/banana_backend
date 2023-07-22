package com.ottention.banana.service.chart;

import com.ottention.banana.repository.TagRepository;
import com.ottention.banana.repository.TopTenTagRepository;
import com.ottention.banana.entity.TopTenTag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChartScheduler {

    private final TagRepository tagRepository;
    private final TopTenTagRepository topTenTagRepository;

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

}
