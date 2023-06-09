package com.ottention.banana.service;

import com.ottention.banana.dto.response.google.GoogleOAuth2UserInfo;
import com.ottention.banana.entity.User;
import com.ottention.banana.exception.UserNotFound;
import com.ottention.banana.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Test
    @DisplayName("구글 회원 저장")
    void saveGoogleUserTest() {
        //given
        GoogleOAuth2UserInfo userInfo = new GoogleOAuth2UserInfo("test", "test1234@gmail.com");

        //when
        Long id = userService.saveFromGoogle(userInfo);
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFound::new);

        //then
        assertThat(user.getNickName()).isEqualTo("test");
        assertThat(user.getEmail()).isEqualTo("test1234@gmail.com");
    }

    @Test
    @DisplayName("이미 저장된 회원은 저장하지 않고 기존 id 값을 반환")
    void duplicateUserSaveTest() {
        //given
        GoogleOAuth2UserInfo userInfo = new GoogleOAuth2UserInfo("test", "test1234@gmail.com");

        //when
        Long id1 = userService.saveFromGoogle(userInfo);
        Long id2 = userService.saveFromGoogle(userInfo);

        //then
        assertThat(id1).isEqualTo(id2);
    }

}