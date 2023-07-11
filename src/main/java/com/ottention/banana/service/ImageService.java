package com.ottention.banana.service;

import com.ottention.banana.dto.request.SaveBusinessCardRequest;
import com.ottention.banana.dto.ImageDto;
import com.ottention.banana.entity.BusinessCard;
import com.ottention.banana.entity.Image;
import com.ottention.banana.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageService {

    private final ImageRepository imageRepository;

    @Transactional
    public void saveImage(SaveBusinessCardRequest request, BusinessCard businessCard) {
        List<ImageDto> frontImages = request.getFrontImages();
        for (ImageDto frontImage : frontImages) {
            log.info("imageUrl = {} ", frontImage.getImageUrl());
            log.info("isFront = {} ", frontImage.getIsFront());
            Image image = Image.createImage(frontImage.getImageUrl(), frontImage.getIsFront(), businessCard, frontImage.getCoordinate());
            imageRepository.save(image);
        }

        List<ImageDto> backImages = request.getBackImages();
        for (ImageDto backImage : backImages) {
            log.info("imageUrl = {} ", backImage.getImageUrl());
            log.info("isFront = {} ", backImage.getIsFront());
            Image image = Image.createImage(backImage.getImageUrl(), backImage.getIsFront(), businessCard, backImage.getCoordinate());
            imageRepository.save(image);
        }
    }

    public List<Image> getFrontImages(Long businessCardId) {
        return imageRepository.findByBusinessCardIdAndIsFront(businessCardId, true);
    }

    public List<Image> getBackImages(Long businessCardId) {
        return imageRepository.findByBusinessCardIdAndIsFront(businessCardId, false);
    }

    @Transactional
    public void deleteImages(List<Image> frontImages, List<Image> backImages) {
        for (Image frontImage : frontImages) {
            imageRepository.delete(frontImage);
        }
        for (Image backImage : backImages) {
            imageRepository.delete(backImage);
        }
    }

}
