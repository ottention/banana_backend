package com.ottention.banana.service;

import com.ottention.banana.dto.BusinessCardContentDto;
import com.ottention.banana.dto.ImageDto;
import com.ottention.banana.dto.LinkDto;
import com.ottention.banana.dto.request.SaveBusinessCardRequest;
import com.ottention.banana.dto.response.businesscard.BusinessCardResponse;
import com.ottention.banana.entity.*;
import com.ottention.banana.exception.BusinessCardNotFound;
import com.ottention.banana.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.ottention.banana.entity.ContentSize.H1;
import static com.ottention.banana.entity.ContentSize.H2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@Transactional
@SpringBootTest
class BusinessCardServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private BusinessCardContentRepository businessCardContentRepository;

    @Autowired
    private BusinessCardRepository businessCardRepository;

    @Autowired
    private BusinessCardService businessCardService;

    private User user;

    @BeforeEach
    void init() {
        user = User.builder()
                .email("a")
                .nickName("a")
                .build();

        userRepository.save(user);
    }

    @Test
    @DisplayName("명함 제작 테스트")
    void saveBusinessCardTest() {
        //given
        BusinessCard businessCard = BusinessCard.builder()
                .frontTemplateColor("#12345")
                .backTemplateColor("#54321")
                .user(user)
                .isRepresent(true)
                .isPublic(true)
                .build();

        BusinessCardContent frontContent = BusinessCardContent.builder()
                .isFront(true)
                .coordinate(new Coordinate(10, 10))
                .contentSize(H1)
                .content("명함 앞 내용")
                .build();

        List<BusinessCardContentDto> frontContents = new ArrayList<>();
        BusinessCardContentDto frontContentDto = new BusinessCardContentDto(frontContent);
        frontContents.add(frontContentDto);

        Link frontLink = Link.builder()
                .businessCard(businessCard)
                .link("명함 앞 링크")
                .isFront(true)
                .coordinate(new Coordinate(10, 10))
                .build();

        List<LinkDto> frontLinks = new ArrayList<>();
        LinkDto frontLinkDto = new LinkDto(frontLink);
        frontLinks.add(frontLinkDto);

        Image frontImage = Image.builder()
                .isFront(true)
                .imageUrl("frontImage.png")
                .businessCard(businessCard)
                .coordinate(new Coordinate(20, 20))
                .build();

        List<ImageDto> frontImages = new ArrayList<>();
        ImageDto frontImageDto = new ImageDto(frontImage);
        frontImages.add(frontImageDto);

        BusinessCardContent backContent = BusinessCardContent.builder()
                .isFront(false)
                .coordinate(new Coordinate(10, 10))
                .contentSize(ContentSize.H2)
                .content("명함 뒤 내용")
                .build();

        List<BusinessCardContentDto> backContents = new ArrayList<>();
        BusinessCardContentDto backContentDto = new BusinessCardContentDto(backContent);
        backContents.add(backContentDto);

        Link backLink = Link.builder()
                .businessCard(businessCard)
                .link("명함 뒤 링크")
                .isFront(false)
                .coordinate(new Coordinate(10, 10))
                .build();

        List<LinkDto> backLinks = new ArrayList<>();
        LinkDto backLinkDto = new LinkDto(backLink);
        backLinks.add(backLinkDto);

        Image backImage = Image.builder()
                .isFront(false)
                .imageUrl("backImage.png")
                .businessCard(businessCard)
                .coordinate(new Coordinate(20, 20))
                .build();

        List<ImageDto> backImages = new ArrayList<>();
        ImageDto backImageDto = new ImageDto(backImage);
        backImages.add(backImageDto);

        List<String> tags = new ArrayList<>();
        tags.add("잇타");

        SaveBusinessCardRequest request = new SaveBusinessCardRequest(true, true,
                frontContents, frontLinks, frontImages, businessCard.getFrontTemplateColor(),
                backContents, backLinks, backImages, businessCard.getBackTemplateColor(), tags);

        //when
        Long businessCardId = businessCardService.saveBusinessCard(user.getId(), request);
        BusinessCard card = businessCardRepository.findById(businessCardId)
                .orElseThrow(BusinessCardNotFound::new);

        List<BusinessCardContent> findFrontContents = businessCardContentRepository.findByBusinessCardIdAndIsFront(businessCardId, true);
        List<BusinessCardContent> findBackContents = businessCardContentRepository.findByBusinessCardIdAndIsFront(businessCardId, false);

        List<Image> findFrontImage = imageRepository.findByBusinessCardIdAndIsFront(businessCardId, true);
        List<Image> findBackImage = imageRepository.findByBusinessCardIdAndIsFront(businessCardId, false);

        List<Link> findFrontLink = linkRepository.findByBusinessCardIdAndIsFront(businessCardId, true);
        List<Link> findBackLink = linkRepository.findByBusinessCardIdAndIsFront(businessCardId, false);

        //then
        assertThat(card.getIsPublic()).isTrue();
        assertThat(card.getIsRepresent()).isTrue();
        assertThat(card.getFrontTemplateColor()).isEqualTo("#12345");
        assertThat(card.getBackTemplateColor()).isEqualTo("#54321");

        assertThat(findFrontContents.get(0).getContent()).isEqualTo("명함 앞 내용");
        assertThat(findFrontContents.get(0).getCoordinate().getxAxis()).isEqualTo(10);
        assertThat(findFrontContents.get(0).getCoordinate().getyAxis()).isEqualTo(10);
        assertThat(findFrontContents.get(0).getContentSize()).isEqualTo(H1);

        assertThat(findBackContents.get(0).getContent()).isEqualTo("명함 뒤 내용");
        assertThat(findBackContents.get(0).getCoordinate().getxAxis()).isEqualTo(10);
        assertThat(findBackContents.get(0).getCoordinate().getyAxis()).isEqualTo(10);
        assertThat(findBackContents.get(0).getContentSize()).isEqualTo(H2);

        assertThat(findFrontImage.get(0).getImageUrl()).isEqualTo("frontImage.png");
        assertThat(findFrontImage.get(0).getCoordinate().getxAxis()).isEqualTo(20);
        assertThat(findFrontImage.get(0).getCoordinate().getyAxis()).isEqualTo(20);

        assertThat(findBackImage.get(0).getImageUrl()).isEqualTo("backImage.png");
        assertThat(findBackImage.get(0).getCoordinate().getxAxis()).isEqualTo(20);
        assertThat(findBackImage.get(0).getCoordinate().getyAxis()).isEqualTo(20);

        assertThat(findFrontLink.get(0).getLink()).isEqualTo("명함 앞 링크");
        assertThat(findBackLink.get(0).getLink()).isEqualTo("명함 뒤 링크");
    }

    @Test
    @DisplayName("명함 수정 테스트")
    void updateBusinessCardTest() {
        //given
        BusinessCard businessCard = BusinessCard.builder()
                .frontTemplateColor("#12345")
                .backTemplateColor("#54321")
                .user(user)
                .isRepresent(true)
                .isPublic(true)
                .build();

        businessCardRepository.save(businessCard);

        BusinessCardContent frontContent = BusinessCardContent.builder()
                .isFront(true)
                .coordinate(new Coordinate(10, 10))
                .contentSize(H1)
                .content("명함 앞 내용 수정")
                .build();

        List<BusinessCardContentDto> frontContents = new ArrayList<>();
        BusinessCardContentDto frontContentDto = new BusinessCardContentDto(frontContent);
        frontContents.add(frontContentDto);

        Link frontLink = Link.builder()
                .businessCard(businessCard)
                .link("명함 앞 링크 수정")
                .isFront(true)
                .coordinate(new Coordinate(10, 10))
                .build();

        List<LinkDto> frontLinks = new ArrayList<>();
        LinkDto frontLinkDto = new LinkDto(frontLink);
        frontLinks.add(frontLinkDto);

        Image frontImage = Image.builder()
                .isFront(true)
                .imageUrl("editFrontImage.png")
                .businessCard(businessCard)
                .coordinate(new Coordinate(20, 20))
                .build();

        List<ImageDto> frontImages = new ArrayList<>();
        ImageDto frontImageDto = new ImageDto(frontImage);
        frontImages.add(frontImageDto);

        BusinessCardContent backContent = BusinessCardContent.builder()
                .isFront(false)
                .coordinate(new Coordinate(10, 10))
                .contentSize(ContentSize.H2)
                .content("명함 뒤 내용 수정")
                .build();

        List<BusinessCardContentDto> backContents = new ArrayList<>();
        BusinessCardContentDto backContentDto = new BusinessCardContentDto(backContent);
        backContents.add(backContentDto);

        Link backLink = Link.builder()
                .businessCard(businessCard)
                .link("명함 뒤 링크 수정")
                .isFront(false)
                .coordinate(new Coordinate(10, 10))
                .build();

        List<LinkDto> backLinks = new ArrayList<>();
        LinkDto backLinkDto = new LinkDto(backLink);
        backLinks.add(backLinkDto);

        Image backImage = Image.builder()
                .isFront(false)
                .imageUrl("editBackImage.png")
                .businessCard(businessCard)
                .coordinate(new Coordinate(20, 20))
                .build();

        List<ImageDto> backImages = new ArrayList<>();
        ImageDto backImageDto = new ImageDto(backImage);
        backImages.add(backImageDto);

        List<String> tags = new ArrayList<>();
        tags.add("바나나");

        SaveBusinessCardRequest request = new SaveBusinessCardRequest(true, true,
                frontContents, frontLinks, frontImages, businessCard.getFrontTemplateColor(),
                backContents, backLinks, backImages, businessCard.getBackTemplateColor(), tags);

        //when
        businessCardService.updateBusinessCard(user.getId(), businessCard.getId(), request);
        BusinessCardResponse card = businessCardService.getBusinessCard(user.getId(), businessCard.getId());

        //then
        assertThat(card.getIsPublic()).isTrue();
        assertThat(card.getIsRepresent()).isTrue();
        assertThat(card.getFrontTemplateColor()).isEqualTo("#12345");
        assertThat(card.getBackTemplateColor()).isEqualTo("#54321");

        assertThat(card.getFrontContents().get(0).getContent()).isEqualTo("명함 앞 내용 수정");
        assertThat(card.getFrontContents().get(0).getCoordinate().getxAxis()).isEqualTo(10);
        assertThat(card.getFrontContents().get(0).getCoordinate().getyAxis()).isEqualTo(10);
        assertThat(card.getFrontContents().get(0).getContentSize()).isEqualTo("H1");

        assertThat(card.getBackContents().get(0).getContent()).isEqualTo("명함 뒤 내용 수정");
        assertThat(card.getBackContents().get(0).getCoordinate().getxAxis()).isEqualTo(10);
        assertThat(card.getBackContents().get(0).getCoordinate().getyAxis()).isEqualTo(10);
        assertThat(card.getBackContents().get(0).getContentSize()).isEqualTo("H2");

        assertThat(card.getFrontImages().get(0).getImageUrl()).isEqualTo("editFrontImage.png");
        assertThat(card.getFrontImages().get(0).getCoordinate().getxAxis()).isEqualTo(20);
        assertThat(card.getFrontImages().get(0).getCoordinate().getyAxis()).isEqualTo(20);

        assertThat(card.getBackImages().get(0).getImageUrl()).isEqualTo("editBackImage.png");
        assertThat(card.getBackImages().get(0).getCoordinate().getxAxis()).isEqualTo(20);
        assertThat(card.getBackImages().get(0).getCoordinate().getyAxis()).isEqualTo(20);

        assertThat(card.getFrontLinks().get(0).getLink()).isEqualTo("명함 앞 링크 수정");
        assertThat(card.getBackLinks().get(0).getLink()).isEqualTo("명함 뒤 링크 수정");

    }

    @Test
    @DisplayName("명함 삭제 테스트")
    void deleteBusinessCardTest() {
        //given
        BusinessCard businessCard = BusinessCard.builder()
                .frontTemplateColor("#12345")
                .backTemplateColor("#54321")
                .user(user)
                .isRepresent(true)
                .isPublic(true)
                .build();

        businessCardRepository.save(businessCard);

        //when
        businessCardService.deleteBusinessCard(businessCard.getId());

        //then
        assertThatThrownBy(() -> businessCardService.getBusinessCard(user.getId(), businessCard.getId()))
                .isInstanceOf(BusinessCardNotFound.class);
    }

}