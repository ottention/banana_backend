package com.ottention.banana.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ottention.banana.dto.request.SaveGuestBookRequest;
import com.ottention.banana.entity.BusinessCard;
import com.ottention.banana.entity.GuestBook;
import com.ottention.banana.entity.User;
import com.ottention.banana.repository.BusinessCardRepository;
import com.ottention.banana.repository.GuestBookRepository;
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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(RestDocumentationExtension.class)
class GuestBookControllerTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BusinessCardRepository businessCardRepository;

    @Autowired
    GuestBookRepository guestBookRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

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
    @DisplayName("방명록 작성 API 테스트")
    void writeGuestBookControllerTest() throws Exception {
        //given
        User user1 = User.builder()
                .nickName("a")
                .email("a")
                .build();

        User user2 = User.builder()
                .nickName("a")
                .email("a")
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

        String accessToken = jwtService.generateAccessToken(user2.getId());

        BusinessCard businessCard = BusinessCard.builder()
                .isRepresent(true)
                .isPublic(true)
                .user(user1)
                .build();

        businessCardRepository.save(businessCard);

        SaveGuestBookRequest request = new SaveGuestBookRequest("방명록 내용");

        //when
        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/banana/businessCard/{businessCardId}/writeGuestBook", businessCard.getId())
                        .content(json)
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", accessToken))
                .andExpect(status().isOk())
                .andDo(document("guestBook-save",
                        requestFields(
                                fieldWithPath("content").description("방명록 내용")
                        )));
    }

    @Test
    @DisplayName("자신 명함에 방명록 작성 오류 테스트")
    void selfGuestBookNotAllowedExceptionTest() throws Exception {
        //given
        User user = User.builder()
                .nickName("a")
                .email("a")
                .build();

        userRepository.save(user);

        String accessToken = jwtService.generateAccessToken(user.getId());

        BusinessCard businessCard = BusinessCard.builder()
                .isRepresent(true)
                .isPublic(true)
                .user(user)
                .build();

        businessCardRepository.save(businessCard);

        SaveGuestBookRequest request = new SaveGuestBookRequest("방명록 내용");

        //when
        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/banana/businessCard/{businessCardId}/writeGuestBook", businessCard.getId())
                        .content(json)
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", accessToken))
                .andExpect(status().isForbidden())
                .andDo(document("guestBook-forbidden"));
    }

    @Test
    @DisplayName("자신이 작성한 방명록 조회 API 테스트")
    void getMyWrittenGuestBooksApiTest() throws Exception {
        //given
        User user1 = User.builder()
                .email("a")
                .nickName("a")
                .build();

        User user2 = User.builder()
                .email("a")
                .nickName("a")
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

        String accessToken = jwtService.generateAccessToken(user2.getId());

        BusinessCard businessCard = BusinessCard.builder()
                .user(user1)
                .isPublic(true)
                .isRepresent(true)
                .build();

        businessCardRepository.save(businessCard);

        List<GuestBook> guestBooks = IntStream.range(0, 20).mapToObj(i -> GuestBook.builder()
                        .content("방명록 내용" + i)
                        .guestBookLike(false)
                        .user(user2)
                        .businessCard(businessCard)
                        .writer(user2.getNickName())
                        .build())
                .collect(Collectors.toList());

        guestBookRepository.saveAll(guestBooks);

        //expected
        mockMvc.perform(RestDocumentationRequestBuilders.get("/banana/myWrittenGuestBooks?page=0g&sort=id,desc")
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value("방명록 내용19"))
                .andDo(document("guestBook-getMyWrittenGuestBooks"));
    }

    @Test
    @DisplayName("자신의 명함 방명록 조회 API 테스트")
    void getBusinessCardGuestBookTest() throws Exception {
        //given
        User user1 = User.builder()
                .email("a")
                .nickName("a")
                .build();

        User user2 = User.builder()
                .email("a")
                .nickName("a")
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

        String accessToken = jwtService.generateAccessToken(user2.getId());

        BusinessCard businessCard = BusinessCard.builder()
                .user(user1)
                .isPublic(true)
                .isRepresent(true)
                .build();

        businessCardRepository.save(businessCard);

        List<GuestBook> guestBooks = IntStream.range(0, 20).mapToObj(i -> GuestBook.builder()
                        .content("방명록 내용" + i)
                        .guestBookLike(false)
                        .user(user2)
                        .businessCard(businessCard)
                        .writer(user2.getNickName())
                        .build())
                .collect(Collectors.toList());

        guestBookRepository.saveAll(guestBooks);

        //expected
        mockMvc.perform(get("/banana/businessCard/{businessCardId}/guestBook?page=0&sort=id,desc", businessCard.getId())
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", accessToken))
                .andExpect(jsonPath("$[0].content").value("방명록 내용19"))
                .andDo(document("guestBook-getBusinessCardGuestBook"));
    }
}