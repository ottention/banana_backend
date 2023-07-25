package com.ottention.banana.service;

import com.ottention.banana.dto.LinkDto;
import com.ottention.banana.dto.request.SaveBusinessCardRequest;
import com.ottention.banana.entity.BusinessCard;
import com.ottention.banana.entity.Coordinate;
import com.ottention.banana.entity.Link;
import com.ottention.banana.entity.User;
import com.ottention.banana.repository.BusinessCardRepository;
import com.ottention.banana.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class LinkServiceTest {

    @Autowired
    private BusinessCardRepository businessCardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LinkService linkService;

    @Test
    @DisplayName("링크 저장, 조회 테스트")
    void saveLinkTest() {
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

        Link frontLink = Link.builder()
                .businessCard(businessCard)
                .link("명함 앞 링크")
                .isFront(true)
                .coordinate(new Coordinate(10, 10))
                .build();

        List<LinkDto> frontLinks = new ArrayList<>();
        LinkDto frontLinkDto = new LinkDto(frontLink);
        frontLinks.add(frontLinkDto);

        Link backLink = Link.builder()
                .businessCard(businessCard)
                .link("명함 뒤 링크")
                .isFront(false)
                .coordinate(new Coordinate(20, 20))
                .build();

        List<LinkDto> backLinks = new ArrayList<>();
        LinkDto backLinkDto = new LinkDto(backLink);
        backLinks.add(backLinkDto);

        SaveBusinessCardRequest request = new SaveBusinessCardRequest(null, null, null, frontLinks, null, null,
                null, backLinks, null, null, null);

        //when
        linkService.saveLink(request, businessCard);
        List<Link> fronts = linkService.getFrontLinks(businessCard.getId());
        List<Link> backs = linkService.getBackLinks(businessCard.getId());

        //then
        assertThat(fronts.get(0).getLink()).isEqualTo("명함 앞 링크");
        assertThat(fronts.get(0).getIsFront()).isTrue();
        assertThat(fronts.get(0).getCoordinate().getxAxis()).isEqualTo(10);
        assertThat(fronts.get(0).getCoordinate().getyAxis()).isEqualTo(10);

        assertThat(backs.get(0).getLink()).isEqualTo("명함 뒤 링크");
        assertThat(backs.get(0).getIsFront()).isFalse();
        assertThat(backs.get(0).getCoordinate().getxAxis()).isEqualTo(20);
        assertThat(backs.get(0).getCoordinate().getyAxis()).isEqualTo(20);
    }

}