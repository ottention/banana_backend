package com.ottention.banana.service;

import com.ottention.banana.dto.request.*;
import com.ottention.banana.entity.BusinessCard;
import com.ottention.banana.entity.BusinessCardContent;
import com.ottention.banana.entity.ContentSize;
import com.ottention.banana.repository.BusinessCardContentRepository;
import com.ottention.banana.dto.BusinessCardContentDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BusinessCardContentService {

    private final BusinessCardContentRepository businessCardContentRepository;

    public void saveBusinessCardContents(SaveBusinessCardRequest request, BusinessCard businessCard) {
        saveContents(request.getFrontContents(), businessCard);
        saveContents(request.getBackContents(), businessCard);
    }

    private void saveContents(List<BusinessCardContentDto> contents, BusinessCard businessCard) {
        for (BusinessCardContentDto content : contents) {
            log.info("content = {}", content.getContent());
            log.info("contentSize = {} ", content.getContentSize());
            log.info("coordinate = {}", content.getCoordinate().getxAxis());
            //String -> enum으로 변경 ex h1으로 들어오면 H1으로 변경
            ContentSize contentSize = ContentSize.fromString(content.getContentSize());

            BusinessCardContent businessCardContent = BusinessCardContent.updateBusinessCardContent(content.getContent(),
                    contentSize, content.getCoordinate(), content.getIsFront());

            businessCardContent.addBusinessCard(businessCard);
            businessCardContentRepository.save(businessCardContent);
        }
    }

    public List<BusinessCardContent> getFrontContents(Long businessCardId) {
        return businessCardContentRepository.findByBusinessCardIdAndIsFront(businessCardId, true);
    }

    public List<BusinessCardContent> getBackContents(Long businessCardId) {
        return businessCardContentRepository.findByBusinessCardIdAndIsFront(businessCardId, false);
    }

}
