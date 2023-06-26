package com.ottention.banana.service;

import com.ottention.banana.dto.request.SaveBackBusinessCardRequest;
import com.ottention.banana.dto.request.SaveFrontBusinessCardRequest;
import com.ottention.banana.dto.request.SaveTagRequest;
import com.ottention.banana.dto.response.businesscard.BusinessCardResponse;
import com.ottention.banana.dto.response.businesscard.TagResponse;
import com.ottention.banana.entity.BusinessCard;
import com.ottention.banana.entity.BusinessCardContent;
import com.ottention.banana.entity.Image;
import com.ottention.banana.entity.User;
import com.ottention.banana.exception.BusinessCardNotFound;
import com.ottention.banana.exception.UserNotFound;
import com.ottention.banana.repository.BusinessCardRepository;
import com.ottention.banana.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BusinessCardService {

    private final BusinessCardContentService businessCardContentService;
    private final ImageService imageService;
    private final TagService tagService;
    private final BusinessCardRepository businessCardRepository;
    private final UserRepository userRepository;

    /**
     * @param userId
     * @param frontRequest : 명함 앞 요청
     * @param backRequest  : 명함 뒤 요청
     * @param frontImages  : 명함 앞 이미지
     * @param backImages   : 명함 뒤 이미지
     * @return
     */
    @Transactional
    public Long save(Long userId, SaveFrontBusinessCardRequest frontRequest, SaveBackBusinessCardRequest backRequest,
                     List<MultipartFile> frontImages, List<MultipartFile> backImages, SaveTagRequest tagRequest) {
        User findUser = userRepository.findById(userId)
                .orElseThrow(UserNotFound::new);

        BusinessCard businessCard = BusinessCard.builder()
                .isPublic(frontRequest.getIsPublic())
                .isRepresent(frontRequest.getIsPresent())
                .user(findUser)
                .build();

        businessCardContentService.saveBusinessCardContents(frontRequest, backRequest, businessCard);
        imageService.saveBusinessCardImages(frontRequest.getFrontImageCoordinates(), backRequest.getBackImageCoordinates(),
                frontImages, backImages, businessCard);
        tagService.saveTag(tagRequest, businessCard);

        return businessCardRepository.save(businessCard).getId();
    }

    /**
     * 명함 앞 데이터 가져오기
     */
    public BusinessCardResponse getFrontBusinessCard(Long businessCardId) {
        return getBusinessCardResponse(businessCardId, true);
    }

    /**
     * 명함 뒤 데이터 가져오기
     */
    public BusinessCardResponse getBackBusinessCard(Long businessCardId) {
        return getBusinessCardResponse(businessCardId, false);
    }

    /**
     * 명함 데이터 가져오기
     * @param businessCardId : 어떤 명함 데이터 가져올건지
     * @param isFront : 앞 뒤 구분을 위한 파라미터
     * @return
     */
    private BusinessCardResponse getBusinessCardResponse(Long businessCardId, boolean isFront) {
        BusinessCard businessCard = businessCardRepository.findById(businessCardId)
                .orElseThrow(BusinessCardNotFound::new);

        List<Image> images = imageService.findByBusinessCardIdAndIsFront(businessCard.getId(), isFront);
        List<BusinessCardContent> contents = businessCardContentService.findByBusinessCardIdAndIsFront(businessCard.getId(), isFront);
        List<TagResponse> tags = tagService.getTags(businessCard.getId());

        return BusinessCardResponse.toBusinessCard(contents, images, tags);
    }

}
