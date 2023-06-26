package com.ottention.banana.service;

import com.ottention.banana.dto.BusinessCardContentDto;
import com.ottention.banana.dto.request.SaveBackBusinessCardRequest;
import com.ottention.banana.dto.request.SaveFrontBusinessCardRequest;
import com.ottention.banana.dto.request.SaveTagRequest;
import com.ottention.banana.dto.response.businesscard.BusinessCardResponse;
import com.ottention.banana.entity.*;
import com.ottention.banana.exception.BusinessCardNotFound;
import com.ottention.banana.repository.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class BusinessCardServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private BusinessCardTagRepository businessCardTagRepository;

    @Autowired
    private BusinessCardContentRepository businessCardContentRepository;

    @Autowired
    private BusinessCardRepository businessCardRepository;

    @Autowired
    private BusinessCardService businessCardService;

    @BeforeEach
    void clean() {
        tagRepository.deleteAll();
        businessCardTagRepository.deleteAll();
        imageRepository.deleteAll();
        businessCardContentRepository.deleteAll();
        businessCardRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("명함 제작 테스트")
    void saveBusinessCardTest() {
        //given
        User user = User.builder()
                .email("a")
                .nickName("a")
                .build();

        userRepository.save(user);

        Coordinate coordinate = Coordinate.builder()
                .xAxis(20)
                .yAxis(20)
                .build();

        BusinessCardContent businessCardContent = BusinessCardContent.builder()
                .coordinate(coordinate)
                .contentSize(ContentSize.H1)
                .content("내용")
                .build();

        //명함 앞
        List<BusinessCardContentDto> contentDtos = IntStream.range(0, 3).mapToObj(i -> BusinessCardContentDto.builder()
                        .businessCardContent(businessCardContent)
                        .build())
                .collect(Collectors.toList());

        List<Coordinate> frontCoordinates = new ArrayList<>();
        Coordinate frontImageCoordinate = new Coordinate(10, 10);
        frontCoordinates.add(frontImageCoordinate);

        SaveFrontBusinessCardRequest front = new SaveFrontBusinessCardRequest();
        front.setIsPresent(true);
        front.setIsPublic(true);
        front.setContents(contentDtos);
        front.setFrontImageCoordinates(frontCoordinates);

        //명함 뒤
        List<Coordinate> backCoordinates = new ArrayList<>();
        Coordinate backImageCoordinate = new Coordinate(20, 20);
        backCoordinates.add(backImageCoordinate);

        SaveBackBusinessCardRequest back = new SaveBackBusinessCardRequest();
        back.setContents(contentDtos);
        back.setBackImageCoordinates(backCoordinates);

        MockMultipartFile mockFrontImage = new MockMultipartFile("frontImage", "frontImage1.jpg", "image/jpeg", "Front Image 1".getBytes());
        MockMultipartFile mockBackImage = new MockMultipartFile("backImage", "backImage2.jpg", "image/jpeg", "Front Image 2".getBytes());

        List<MultipartFile> frontImages = new ArrayList<>();
        frontImages.add(mockFrontImage);

        List<MultipartFile> backImages = new ArrayList<>();
        backImages.add(mockBackImage);

        SaveTagRequest tagRequest = new SaveTagRequest();
        List<String> tags = new ArrayList<>();
        tags.add("잇타");
        tags.add("바나나");
        tagRequest.setTags(tags);

        //when
        Long businessCardId = businessCardService.save(user.getId(), front, back, frontImages, backImages, tagRequest);
        BusinessCard businessCard = businessCardRepository.findById(businessCardId)
                .orElseThrow(BusinessCardNotFound::new);

        List<Image> frontImageList = imageRepository.findByBusinessCardIdAndIsFront(businessCardId, true);
        List<Image> backImageList = imageRepository.findByBusinessCardIdAndIsFront(businessCardId, false);

        List<BusinessCardContent> frontContents = businessCardContentRepository.findByBusinessCardIdAndIsFront(businessCardId, true);
        List<BusinessCardContent> backContents = businessCardContentRepository.findByBusinessCardIdAndIsFront(businessCardId, false);

        //then
        assertThat(businessCard.getIsPublic()).isTrue();
        assertThat(businessCard.getIsRepresent()).isTrue();
        assertThat(businessCard.getUser().getId()).isEqualTo(user.getId());

        assertThat(frontContents.size()).isEqualTo(3);
        assertThat(backContents.size()).isEqualTo(3);

        assertThat(frontImageList.get(0).getIsFront()).isTrue();
        assertThat(frontImageList.get(0).getCoordinate().getxAxis()).isEqualTo(10);
        assertThat(frontImageList.get(0).getCoordinate().getyAxis()).isEqualTo(10);
        assertThat(backImageList.get(0).getCoordinate().getxAxis()).isEqualTo(20);
        assertThat(backImageList.get(0).getCoordinate().getyAxis()).isEqualTo(20);
    }

    @Test
    @DisplayName("명함 앞 뒤 조회 테스트")
    void getBusinessCardTest() {
        //given
        User user = User.builder()
                .email("a")
                .nickName("a")
                .build();

        userRepository.save(user);

        BusinessCard businessCard = BusinessCard.builder()
                .user(user)
                .isPublic(true)
                .isRepresent(true)
                .build();

        businessCardRepository.save(businessCard);

        BusinessCardContent frontContent = BusinessCardContent
                .builder()
                .isFront(true)
                .coordinate(new Coordinate(10, 10))
                .content("a")
                .contentSize(ContentSize.H1)
                .build();

        frontContent.addBusinessCard(businessCard);

        BusinessCardContent backContent = BusinessCardContent
                .builder()
                .isFront(false)
                .coordinate(new Coordinate(20, 20))
                .content("b")
                .contentSize(ContentSize.H2)
                .build();

        backContent.addBusinessCard(businessCard);

        businessCardContentRepository.save(frontContent);
        businessCardContentRepository.save(backContent);

        BusinessCardTag businessCardTag = new BusinessCardTag();
        Tag createdTag = new Tag();

        createdTag.updateName("잇타");
        createdTag.addTag(businessCardTag, businessCard);

        businessCardTagRepository.save(businessCardTag);
        tagRepository.save(createdTag);

        Image frontImage = Image.builder()
                .isFront(true)
                .businessCard(businessCard)
                .coordinate(new Coordinate(10, 10))
                .imageUrl("frontImage.png")
                .build();

        imageRepository.save(frontImage);

        Image backImage = Image.builder()
                .isFront(false)
                .businessCard(businessCard)
                .coordinate(new Coordinate(20, 20))
                .imageUrl("backImage.png")
                .build();

        imageRepository.save(backImage);

        //when
        BusinessCardResponse frontResponse = businessCardService.getFrontBusinessCard(businessCard.getId());
        BusinessCardResponse backResponse = businessCardService.getBackBusinessCard(businessCard.getId());

        //then
        assertThat(frontResponse.getContents().get(0).getContent()).isEqualTo("a");
        assertThat(frontResponse.getContents().get(0).getContentSize()).isEqualTo("H1");
        assertThat(frontResponse.getContents().get(0).getCoordinate().getxAxis()).isEqualTo(10);
        assertThat(frontResponse.getContents().get(0).getCoordinate().getyAxis()).isEqualTo(10);
        assertThat(frontResponse.getTags().get(0).getTag()).isEqualTo("잇타");
        assertThat(frontResponse.getImages().get(0).getImageUrl()).isEqualTo("frontImage.png");
        assertThat(frontResponse.getImages().get(0).getCoordinate().getxAxis()).isEqualTo(10);
        assertThat(frontResponse.getImages().get(0).getCoordinate().getyAxis()).isEqualTo(10);

        assertThat(backResponse.getContents().get(0).getContent()).isEqualTo("b");
        assertThat(backResponse.getContents().get(0).getContentSize()).isEqualTo("H2");
        assertThat(backResponse.getContents().get(0).getCoordinate().getxAxis()).isEqualTo(20);
        assertThat(backResponse.getContents().get(0).getCoordinate().getyAxis()).isEqualTo(20);
        assertThat(backResponse.getTags().get(0).getTag()).isEqualTo("잇타");
        assertThat(backResponse.getImages().get(0).getImageUrl()).isEqualTo("backImage.png");
        assertThat(backResponse.getImages().get(0).getCoordinate().getxAxis()).isEqualTo(20);
        assertThat(backResponse.getImages().get(0).getCoordinate().getyAxis()).isEqualTo(20);
    }

}