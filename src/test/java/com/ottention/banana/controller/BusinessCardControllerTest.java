package com.ottention.banana.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ottention.banana.dto.BusinessCardContentDto;
import com.ottention.banana.dto.ImageDto;
import com.ottention.banana.dto.LinkDto;
import com.ottention.banana.dto.request.SaveBusinessCardRequest;
import com.ottention.banana.entity.*;
import com.ottention.banana.repository.BusinessCardRepository;
import com.ottention.banana.repository.UserRepository;
import com.ottention.banana.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.ArrayList;
import java.util.List;

import static com.ottention.banana.entity.ContentSize.H1;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(RestDocumentationExtension.class)
public class BusinessCardControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BusinessCardRepository businessCardRepository;

    @Autowired
    private JwtService jwtService;

    private MockMvc mockMvc;
    private User user;
    private String accessToken;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
               RestDocumentationContextProvider restDocumentation) {
        user = User.builder()
                .nickName("a")
                .email("a")
                .build();

        userRepository.save(user);

        accessToken = jwtService.generateAccessToken(user.getId());

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(print())
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    @Test
    @DisplayName("/businessCard/save 테스트")
    void saveBusinessCardControllerTest() throws Exception {
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

        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(MockMvcRequestBuilders.post("/businessCard/save")
                        .header("Authorization", accessToken)
                        .content(content)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("/businessCard/{businessCardId} 테스트")
    void getBusinessCardControllerTest() throws Exception {
        //given
        BusinessCard businessCard = BusinessCard.builder()
                .frontTemplateColor("#12345")
                .backTemplateColor("#54321")
                .user(user)
                .isRepresent(true)
                .isPublic(true)
                .build();

        businessCardRepository.save(businessCard);

        //expected
        mockMvc.perform(MockMvcRequestBuilders.get("/businessCard/{businessCardId}", businessCard.getId())
                .header("Authorization", accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.businessCardId").value(businessCard.getId()))
                .andExpect(jsonPath("$.frontTemplateColor").value("#12345"))
                .andExpect(jsonPath("$.backTemplateColor").value("#54321"))
                .andExpect(jsonPath("$.isPublic").value("true"))
                .andExpect(jsonPath("$.isRepresent").value("true"));
    }

    @Test
    @DisplayName("/businessCard/{businessCardId}/update 테스트")
    void updateBusinessCardControllerTest() throws Exception {
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

        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(MockMvcRequestBuilders.patch("/businessCard/{businessCardId}/update", businessCard.getId())
                        .header("Authorization", accessToken)
                        .content(content)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("/businessCard/{businessCardId}/delete 테스트")
    void deleteBusinessCardControllerTest() throws Exception {
        //given
        BusinessCard businessCard = BusinessCard.builder()
                .frontTemplateColor("#12345")
                .backTemplateColor("#54321")
                .user(user)
                .isRepresent(true)
                .isPublic(true)
                .build();

        businessCardRepository.save(businessCard);

        //expected
        mockMvc.perform(MockMvcRequestBuilders.delete("/businessCard/{businessCardId}/delete", businessCard.getId())
                        .header("Authorization", accessToken))
                .andExpect(status().isOk());
    }

}
