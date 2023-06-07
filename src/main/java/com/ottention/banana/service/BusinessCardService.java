package com.ottention.banana.service;

import com.ottention.banana.dto.request.SaveBackBusinessCardRequest;
import com.ottention.banana.dto.request.SaveFrontBusinessCardRequest;
import com.ottention.banana.dto.response.businesscard.BusinessCardResponse;
import com.ottention.banana.entity.BusinessCard;
import com.ottention.banana.entity.BusinessCardContent;
import com.ottention.banana.entity.Image;
import com.ottention.banana.entity.User;
import com.ottention.banana.exception.BusinessCardNotFound;
import com.ottention.banana.exception.UserNotFound;
import com.ottention.banana.repository.BusinessCardRepository;
import com.ottention.banana.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BusinessCardService {

    private final BusinessCardContentService businessCardContentService;
    private final BusinessCardRepository businessCardRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;

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
                     List<MultipartFile> frontImages, List<MultipartFile> backImages) {
        User findUser = userRepository.findById(userId)
                .orElseThrow(UserNotFound::new);

        BusinessCard businessCard = BusinessCard.builder()
                .isPublic(frontRequest.getIsPublic())
                .isRepresent(frontRequest.getIsPresent())
                .user(findUser)
                .build();

        businessCardContentService.saveBusinessCardContents(frontRequest, backRequest, businessCard);
        imageService.saveBusinessCardImages(frontImages, backImages, businessCard);

        return businessCardRepository.save(businessCard).getId();
    }

    /**
     * 명함 앞 데이터 가져오기
     */
    public BusinessCardResponse getFrontBusinessCard(Long id) {
        return getBusinessCardResponse(id, true);
    }

    /**
     * 명함 뒤 데이터 가져오기
     */
    public BusinessCardResponse getBackBusinessCard(Long id) {
        return getBusinessCardResponse(id, false);
    }

    /**
     * 명함 데이터 가져오기
     * @param id : 어떤 명함 데이터 가져올건지
     * @param isFront : 앞 뒤 구분을 위한 파라미터
     * @return
     */
    private BusinessCardResponse getBusinessCardResponse(Long id, boolean isFront) {
        BusinessCard businessCard = businessCardRepository.findById(id)
                .orElseThrow(BusinessCardNotFound::new);

        List<Image> images = imageService.findByBusinessCardIdAndIsFront(businessCard.getId(), isFront);
        List<String> imageUrls = imageService.getImageUrls(images);
        List<BusinessCardContent> contents = businessCardContentService.findByBusinessCardIdAndIsFront(businessCard.getId(), isFront);

        return BusinessCardResponse.toBusinessCard(contents, imageUrls);
    }

}
