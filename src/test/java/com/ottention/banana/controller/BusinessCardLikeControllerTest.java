package com.ottention.banana.controller;

import com.ottention.banana.entity.BusinessCard;
import com.ottention.banana.entity.User;
import com.ottention.banana.repository.BusinessCardRepository;
import com.ottention.banana.repository.UserRepository;
import com.ottention.banana.service.BusinessCardLikeService;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(RestDocumentationExtension.class)
class BusinessCardLikeControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BusinessCardRepository businessCardRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private BusinessCardLikeService businessCardLikeService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
               RestDocumentationContextProvider restDocumentation) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(print())
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    @Test
    @DisplayName("/businessCard/{businessCardId}/like 테스트")
    void likeControllerTest() throws Exception {
        //given
        User user = User.builder()
                .nickName("a")
                .email("a")
                .build();

        userRepository.save(user);

        String accessToken = jwtService.generateAccessToken(user.getId());

        BusinessCard businessCard = BusinessCard.builder()
                .user(user)
                .isRepresent(true)
                .isPublic(true)
                .build();

        businessCardRepository.save(businessCard);

        //expected
        mockMvc.perform(post("/businessCard/{businessCardId}/like", businessCard.getId())
                        .header("Authorization", accessToken))
                .andExpect(status().isOk())
                .andDo(document("like"));
    }

    @Test
    @DisplayName("/businessCard/{businessCardId}/like 좋아요 중복 테스트")
    void duplicationLikeExceptionTest() throws Exception {
        //given
        User user = User.builder()
                .nickName("a")
                .email("a")
                .build();

        userRepository.save(user);

        String accessToken = jwtService.generateAccessToken(user.getId());

        BusinessCard businessCard = BusinessCard.builder()
                .user(user)
                .isRepresent(true)
                .isPublic(true)
                .build();

        businessCardRepository.save(businessCard);
        businessCardLikeService.like(user.getId(), businessCard.getId());

        //expected
        mockMvc.perform(post("/businessCard/{businessCardId}/like", businessCard.getId())
                        .header("Authorization", accessToken))
                .andExpect(status().isConflict())
                .andDo(document("duplicationLike"));
    }

    @Test
    @DisplayName("/businessCard/{businessCardId}/cancelLike 테스트")
    void cancelLikeControllerTest() throws Exception {
        //given
        User user = User.builder()
                .nickName("a")
                .email("a")
                .build();

        userRepository.save(user);

        String accessToken = jwtService.generateAccessToken(user.getId());

        BusinessCard businessCard = BusinessCard.builder()
                .user(user)
                .isRepresent(true)
                .isPublic(true)
                .build();

        businessCardRepository.save(businessCard);

        businessCardLikeService.like(user.getId(), businessCard.getId());

        //expected
        mockMvc.perform(delete("/businessCard/{businessCardId}/cancelLike", businessCard.getId())
                        .header("Authorization", accessToken))
                .andExpect(status().isOk())
                .andDo(document("cancelLike"));
    }

    @Test
    @DisplayName("/businessCard/{businessCardId}/cancelLike 예외 테스트")
    void zeroLikesError() throws Exception {
        //given
        User user = User.builder()
                .nickName("a")
                .email("a")
                .build();

        userRepository.save(user);

        String accessToken = jwtService.generateAccessToken(user.getId());

        BusinessCard businessCard = BusinessCard.builder()
                .user(user)
                .isRepresent(true)
                .isPublic(true)
                .build();

        businessCardRepository.save(businessCard);

        //expected
        mockMvc.perform(delete("/businessCard/{businessCardId}/cancelLike", businessCard.getId())
                        .header("Authorization", accessToken))
                .andExpect(status().isBadRequest())
                .andDo(document("zeroLikesError"));
    }

}