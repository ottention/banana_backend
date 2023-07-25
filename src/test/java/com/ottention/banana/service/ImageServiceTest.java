package com.ottention.banana.service;

import com.ottention.banana.dto.ImageDto;
import com.ottention.banana.dto.request.SaveBusinessCardRequest;
import com.ottention.banana.entity.BusinessCard;
import com.ottention.banana.entity.Coordinate;
import com.ottention.banana.entity.Image;
import com.ottention.banana.entity.User;
import com.ottention.banana.repository.BusinessCardRepository;
import com.ottention.banana.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class ImageServiceTest {

    @Autowired
    private BusinessCardRepository businessCardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageService imageService;

    @Test
    @DisplayName("이미지 저장, 조회 테스트")
    void saveAndGetImageTest() {
        //given
        User user = User.builder()
                .email("a")
                .nickName("a")
                .build();

        userRepository.save(user);

        BusinessCard businessCard = BusinessCard.builder()
                .user(user)
                .isRepresent(true)
                .isPublic(true)
                .backTemplateColor("#12345")
                .frontTemplateColor("54321")
                .build();

        businessCardRepository.save(businessCard);

        Image frontImage = Image.builder()
                .isFront(true)
                .imageUrl("frontImage.png")
                .businessCard(businessCard)
                .coordinate(new Coordinate(10, 10))
                .build();

        List<ImageDto> frontImages = new ArrayList<>();
        ImageDto frontImageDto = new ImageDto(frontImage);
        frontImages.add(frontImageDto);


        Image backImage = Image.builder()
                .isFront(false)
                .imageUrl("backImage.png")
                .businessCard(businessCard)
                .coordinate(new Coordinate(20, 20))
                .build();

        List<ImageDto> backImages = new ArrayList<>();
        ImageDto backImageDto = new ImageDto(backImage);
        backImages.add(backImageDto);

        SaveBusinessCardRequest request = new SaveBusinessCardRequest(null, null, null, null, frontImages, null,
                null, null, backImages, null, null);

        //when
        imageService.saveImage(request, businessCard);
        List<Image> fronts = imageService.getFrontImages(businessCard.getId());
        List<Image> backs = imageService.getBackImages(businessCard.getId());

        //then
        assertThat(fronts.get(0).getImageUrl()).isEqualTo("frontImage.png");
        assertThat(fronts.get(0).getIsFront()).isTrue();
        assertThat(fronts.get(0).getCoordinate().getxAxis()).isEqualTo(10);
        assertThat(fronts.get(0).getCoordinate().getyAxis()).isEqualTo(10);

        assertThat(backs.get(0).getImageUrl()).isEqualTo("backImage.png");
        assertThat(backs.get(0).getIsFront()).isFalse();
        assertThat(backs.get(0).getCoordinate().getxAxis()).isEqualTo(20);
        assertThat(backs.get(0).getCoordinate().getyAxis()).isEqualTo(20);
    }

}