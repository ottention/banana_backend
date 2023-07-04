package com.ottention.banana.service;

import com.ottention.banana.entity.BusinessCard;
import com.ottention.banana.entity.BusinessCardTag;
import com.ottention.banana.entity.Tag;
import com.ottention.banana.exception.TagLimitExceededException;
import com.ottention.banana.repository.BusinessCardTagRepository;
import com.ottention.banana.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.ottention.banana.AppConstant.MAX_TAG_COUNT;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagService {

    private final TagRepository tagRepository;
    private final BusinessCardTagRepository businessCardTagRepository;

    @Transactional
    public void saveTag(List<String> tags, BusinessCard businessCard) {
        if (tags.size() > MAX_TAG_COUNT) {
            throw new TagLimitExceededException();
        }

        for (String tag : tags) {
            log.info("tag = {}", tag);
            BusinessCardTag businessCardTag = new BusinessCardTag();
            Tag createdTag = new Tag();

            createdTag.updateName(tag.replaceAll("\\s", ""));
            createdTag.addTag(businessCardTag, businessCard);

            businessCardTagRepository.save(businessCardTag);
            tagRepository.save(createdTag);
        }
    }

    public List<Tag> getTags(Long businessCardId) {
        List<BusinessCardTag> businessCardTags = businessCardTagRepository.findByBusinessCardId(businessCardId);
        return businessCardTags.stream().map(t -> t.getTag())
                .collect(Collectors.toList());
    }

}
