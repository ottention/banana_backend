package com.ottention.banana.service;

import com.ottention.banana.entity.BusinessCard;
import com.ottention.banana.entity.BusinessCardContent;
import com.ottention.banana.entity.ContentSize;
import com.ottention.banana.repository.BusinessCardContentRepository;
import com.ottention.banana.dto.request.SaveBackBusinessCardRequest;
import com.ottention.banana.dto.request.SaveFrontBusinessCardRequest;
import com.ottention.banana.dto.BusinessCardContentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BusinessCardContentService {

    private final BusinessCardContentRepository businessCardContentRepository;

    /**
     * 명함 앞, 뒤 둘 중 하나에만 데이터를 저장하고 싶어할 수도 있음 -> null 체크 후 저장
     */
    public void saveBusinessCardContents(SaveFrontBusinessCardRequest frontRequest,
                                          SaveBackBusinessCardRequest backRequest, BusinessCard businessCard) {
        if (frontRequest != null) {
            saveContents(frontRequest.getContents(), true, businessCard);
        }

        if (backRequest != null) {
            saveContents(backRequest.getContents(), false, businessCard);
        }
    }

    /**
     * @param contents : 명함 내용 관련 정보들 (명함 내용, 텍스트 박스 크기, x, y 축)
     * @param isFront  : 명함 앞 뒤 구분 true면 앞 false면 뒤
     */
    private void saveContents(List<BusinessCardContentDto> contents, boolean isFront, BusinessCard businessCard) {
        for (BusinessCardContentDto content : contents) {
            //String -> enum으로 변경 ex h1으로 들어오면 H1으로 변경
            ContentSize contentSize = ContentSize.fromString(content.getContentSize());

            BusinessCardContent businessCardContent = BusinessCardContent.updateBusinessCardContent(content.getContent(),
                    contentSize, content.getCoordinate(), isFront);

            businessCardContent.addBusinessCard(businessCard);
            businessCardContentRepository.save(businessCardContent);
        }
    }

    public List<BusinessCardContent> findByBusinessCardIdAndIsFront(Long businessCardId, boolean isFront) {
        return businessCardContentRepository.findByBusinessCardIdAndIsFront(businessCardId, isFront);
    }

}
