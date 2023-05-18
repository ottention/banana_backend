package com.ottention.banana.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.ottention.banana.config.S3Config;
import com.ottention.banana.entity.*;
import com.ottention.banana.exception.BusinessCardNotFound;
import com.ottention.banana.exception.FileUploadException;
import com.ottention.banana.repository.*;
import com.ottention.banana.request.LoginUser;
import com.ottention.banana.request.SaveBusinessCardRequest;
import com.ottention.banana.response.BusinessCardResponse;
import com.ottention.banana.response.ContentResponse;
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

    @Transactional
    public Long save(LoginUser user, SaveBusinessCardRequest request, List<MultipartFile> files) {
        User findUser = userRepository.findById(user.getId())
                .orElseThrow();

        BusinessCard businessCard = BusinessCard.builder()
                .isPublic(request.getIsPublic())
                .user(findUser)
                .build();

        saveBusinessCardContents(request, businessCard);
        saveImages(files, businessCard);
        saveTags(request, businessCard);

        return businessCardRepository.save(businessCard).getId();
    }

    private void saveBusinessCardContents(SaveBusinessCardRequest request, BusinessCard businessCard) {
        List<ContentResponse> contents = request.getContents();
        for (ContentResponse content : contents) {
            BusinessCardContent businessCardContent = BusinessCardContent.builder()
                    .content(content.getContent())
                    .xAxis(content.getxAxis())
                    .yAxis(content.getyAxis())
                    .build();

            businessCardContent.addBusinessCard(businessCard);
            businessCardContentRepository.save(businessCardContent);
        }
    }

    private void saveTags(SaveBusinessCardRequest request, BusinessCard businessCard) {
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

    private void saveImages(List<MultipartFile> files, BusinessCard businessCard) {
        for (MultipartFile file : files) {
            String s3FileName = randomUUID() + "-" + file.getOriginalFilename();
            try {
                ObjectMetadata objMeta = new ObjectMetadata();
                objMeta.setContentLength(file.getInputStream().available());

                amazonS3.putObject(s3Config.getBucket(), s3FileName, file.getInputStream(), objMeta);

                Image image = new Image();
                image.addBusinessCard(businessCard, s3FileName);
                imageRepository.save(image);
            } catch (IOException e) {
                throw new FileUploadException();
            }
        }
    }

    public BusinessCardResponse getBusinessCard(Long id) {
        BusinessCard businessCard = businessCardRepository.findById(id)
                .orElseThrow(BusinessCardNotFound::new);

        List<Image> images = imageRepository.findByBusinessCardId(businessCard.getId());
        List<String> imageUrls = getImageUrls(images);
        List<BusinessCardContent> contents = businessCardContentRepository.findByBusinessCardId(businessCard.getId());
        List<BusinessCardTag> businessCardTags = businessCardTagRepository.findByBusinessCardId(businessCard.getId());

        return BusinessCardResponse.toBusinessCard(contents, imageUrls, businessCardTags);
    }

    private List<String> getImageUrls(List<Image> images) {
        List<String> urls = new ArrayList<>();

        for (Image image : images) {
            String imageUrl = amazonS3.getUrl(s3Config.getBucket(), image.getImageUrl()).toString();
            urls.add(imageUrl);
        }

        return urls;
    }
}
