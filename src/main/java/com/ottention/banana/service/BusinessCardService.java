package com.ottention.banana.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.ottention.banana.config.S3Config;
import com.ottention.banana.entity.*;
import com.ottention.banana.exception.BusinessCardNotFound;
import com.ottention.banana.exception.FileUploadException;
import com.ottention.banana.exception.UserNotFound;
import com.ottention.banana.repository.*;
import com.ottention.banana.request.SaveBackBusinessCardRequest;
import com.ottention.banana.request.SaveFrontBusinessCardRequest;
import com.ottention.banana.request.SaveTagRequest;
import com.ottention.banana.response.businesscard.BusinessCardResponse;
import com.ottention.banana.response.businesscard.ContentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.util.UUID.randomUUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BusinessCardService {

    private final BusinessCardContentRepository businessCardContentRepository;
    private final BusinessCardTagRepository businessCardTagRepository;
    private final BusinessCardRepository businessCardRepository;
    private final ImageRepository imageRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final AmazonS3Client amazonS3;
    private final S3Config s3Config;

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

        saveBusinessCardContents(frontRequest, backRequest, businessCard);
        saveBusinessCardImages(frontImages, backImages, businessCard);

        return businessCardRepository.save(businessCard).getId();
    }

    /**
     * 명함 앞, 뒤 둘 중 하나에만 데이터를 저장하고 싶어할 수도 있음 -> null 체크 후 저장
     */
    private void saveBusinessCardContents(SaveFrontBusinessCardRequest frontRequest,
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
    private void saveContents(List<ContentResponse> contents, boolean isFront, BusinessCard businessCard) {
        for (ContentResponse content : contents) {
            //String -> enum으로 변경 ex h1으로 들어오면 H1으로 변경
            ContentSize contentSize = ContentSize.fromString(content.getContentSize());

            BusinessCardContent businessCardContent = BusinessCardContent.createBusinessCardContent(content.getContent(),
                    contentSize, content.getxAxis(), content.getyAxis(), isFront);

            businessCardContent.addBusinessCard(businessCard);
            businessCardContentRepository.save(businessCardContent);
        }
    }

    /**
     * 태그 저장
     */
    private void saveTags(SaveTagRequest request, BusinessCard businessCard) {
        List<String> tags = request.getTags();
        for (String tag : tags) {
            BusinessCardTag businessCardTag = new BusinessCardTag();
            Tag createdTag = new Tag();

            createdTag.updateName(tag);
            createdTag.addTag(businessCardTag, businessCard);

            businessCardTagRepository.save(businessCardTag);
            tagRepository.save(createdTag);
        }
    }

    /**
     * @param frontImages  : 명함 앞 이미지 파일들
     * @param backImages   : 명함 뒤 이미지 파일들
     * @param businessCard
     */
    private void saveBusinessCardImages(List<MultipartFile> frontImages, List<MultipartFile> backImages, BusinessCard businessCard) {
        saveImages(frontImages, businessCard, true);
        saveImages(backImages, businessCard, false);
    }

    /**
     * AWS S3에 업로드
     *
     * @param files
     * @param businessCard
     * @param isFront
     */
    private void saveImages(List<MultipartFile> files, BusinessCard businessCard, boolean isFront) {
        if (files != null) {
            for (MultipartFile file : files) {
                String s3FileName = generateS3FileName(file);
                try {
                    uploadFileToS3(file, s3FileName);
                    saveImage(businessCard, isFront, s3FileName);
                } catch (IOException e) {
                    throw new FileUploadException();
                }
            }
        }
    }

    /**
     * 이미지 파일 이름 생성
     */
    private String generateS3FileName(MultipartFile file) {
        return randomUUID() + "-" + file.getOriginalFilename();
    }

    /**
     * 이미지 저장
     */
    private void saveImage(BusinessCard businessCard, boolean isFront, String s3FileName) {
        Image image = Image.createImage(s3FileName, isFront, businessCard);
        log.info("image = {} ", image.getImageUrl());
        imageRepository.save(image);
    }

    /**
     * S3 업로드
     * @param file       : 이미지 파일
     * @param s3FileName : 이미지 파일 이름
     */
    private void uploadFileToS3(MultipartFile file, String s3FileName) throws IOException {
        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(file.getInputStream().available());
        amazonS3.putObject(s3Config.getBucket(), s3FileName, file.getInputStream(), objMeta);
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
        BusinessCard businessCard = getBusinessCard(id);

        List<Image> images = imageRepository.findByBusinessCardIdAndIsFront(businessCard.getId(), isFront);
        List<String> imageUrls = getImageUrls(images);
        List<BusinessCardContent> contents = businessCardContentRepository.findByBusinessCardIdAndIsFront(businessCard.getId(), isFront);

        return BusinessCardResponse.toBusinessCard(contents, imageUrls);
    }

    /**
     * 명함 가져오기
     */
    private BusinessCard getBusinessCard(Long id) {
        return businessCardRepository.findById(id)
                .orElseThrow(BusinessCardNotFound::new);
    }

    /**
     * 이미지 URL 가져오는 메서드
     */
    private List<String> getImageUrls(List<Image> images) {
        List<String> urls = new ArrayList<>();

        for (Image image : images) {
            String imageUrl = amazonS3.getUrl(s3Config.getBucket(), image.getImageUrl()).toString();
            urls.add(imageUrl);
        }

        return urls;
    }
}
