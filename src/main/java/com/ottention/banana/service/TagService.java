package com.ottention.banana.service;

import com.ottention.banana.dto.request.SaveTagRequest;
import com.ottention.banana.dto.response.businesscard.TagResponse;
import com.ottention.banana.entity.BusinessCard;
import com.ottention.banana.entity.BusinessCardTag;
import com.ottention.banana.entity.Tag;
import com.ottention.banana.exception.TagLimitExceededException;
import com.ottention.banana.repository.BusinessCardTagRepository;
import com.ottention.banana.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagService {

    private final TagRepository tagRepository;
    private final BusinessCardTagRepository businessCardTagRepository;

    @Transactional
    public void saveTag(SaveTagRequest request, BusinessCard businessCard) {
        List<String> tags = request.getTags();

        if (tags.size() > 10) {
            throw new TagLimitExceededException();
        }

        for (String tag : tags) {
            BusinessCardTag businessCardTag = new BusinessCardTag();
            Tag createdTag = new Tag();

            createdTag.updateName(tag.replaceAll("\\s", ""));
            createdTag.addTag(businessCardTag, businessCard);

            businessCardTagRepository.save(businessCardTag);
            tagRepository.save(createdTag);
        }
    }

    public List<TagResponse> getTags(Long businessCardId) {
        List<BusinessCardTag> businessCardTags = businessCardTagRepository.findByBusinessCardId(businessCardId);
        return businessCardTags.stream().map(t -> new TagResponse(t.getTag().getName()))
                .collect(Collectors.toList());
    }

}
