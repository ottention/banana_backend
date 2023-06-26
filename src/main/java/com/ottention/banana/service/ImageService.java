package com.ottention.banana.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.ottention.banana.config.S3Config;
import com.ottention.banana.entity.BusinessCard;
import com.ottention.banana.entity.Coordinate;
import com.ottention.banana.entity.Image;
import com.ottention.banana.exception.FileUploadException;
import com.ottention.banana.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.util.UUID.randomUUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageService {

    private final ImageRepository imageRepository;
    private final AmazonS3Client amazonS3;
    private final S3Config s3Config;

    /**
     * @param frontImages  : 명함 앞 이미지 파일들
     * @param backImages   : 명함 뒤 이미지 파일들
     * @param businessCard
     */
    @Transactional
    public void saveBusinessCardImages(List<Coordinate> frontImageCoordinates, List<Coordinate> backImageCoordinates,
                                       List<MultipartFile> frontImages, List<MultipartFile> backImages, BusinessCard businessCard) {
        saveImages(frontImageCoordinates, frontImages, businessCard, true);
        saveImages(backImageCoordinates, backImages, businessCard, false);
    }

    /**
     * AWS S3에 업로드
     * @param files
     * @param businessCard
     * @param isFront
     */
    private void saveImages(List<Coordinate> imageCoordinates, List<MultipartFile> files, BusinessCard businessCard, boolean isFront) {
        if (files != null) {
            for (int i = 0; i < files.size(); i++) {
                String s3FileName = randomUUID() + "-" + files.get(i).getOriginalFilename();
                uploadFileToS3(imageCoordinates.get(i), businessCard, isFront, files.get(i), s3FileName);
            }
        }
    }

    /**
     * @param businessCard : 이미지와 연관관계 매핑
     * @param isFront : 앞 뒤 구분
     * @param file : 이미지 파일
     * @param s3FileName : 이미지 파일 이름
     */
    private void uploadFileToS3(Coordinate imageCoordinate, BusinessCard businessCard,
                                boolean isFront, MultipartFile file, String s3FileName) {
        try {
            ObjectMetadata objMeta = new ObjectMetadata();
            objMeta.setContentLength(file.getInputStream().available());
            amazonS3.putObject(s3Config.getBucket(), s3FileName, file.getInputStream(), objMeta);
            saveImage(imageCoordinate, businessCard, isFront, s3FileName);
        } catch (IOException e) {
            throw new FileUploadException();
        }
    }

    /**
     * 이미지 저장
     */
    private void saveImage(Coordinate imageCoordinate, BusinessCard businessCard, boolean isFront, String s3FileName) {
        Image image = Image.createImage(s3FileName, isFront, businessCard, imageCoordinate);
        imageRepository.save(image);
    }
    
    /**
     * 이미지 URL 가져오는 메서드
     */
    public List<String> getImageUrls(List<Image> images) {
        List<String> urls = new ArrayList<>();

        for (Image image : images) {
            String imageUrl = amazonS3.getUrl(s3Config.getBucket(), image.getImageUrl()).toString();
            urls.add(imageUrl);
        }

        return urls;
    }

    public List<Image> findByBusinessCardIdAndIsFront(Long businessCardId, boolean isFront) {
        return imageRepository.findByBusinessCardIdAndIsFront(businessCardId, isFront);
    }

}
