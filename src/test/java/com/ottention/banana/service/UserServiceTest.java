package com.ottention.banana.service;

import com.ottention.banana.entity.User;
import com.ottention.banana.repository.UserRepository;
import com.ottention.banana.response.google.GoogleOAuth2UserInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;


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
                .orElseThrow();

        //then
        assertThat(user.getNickName()).isEqualTo("test");
        assertThat(user.getEmail()).isEqualTo("test1234@gmail.com");
    }

    @Test
    @DisplayName("이미 저장된 회원은 저장하지 않고 기존 id 값을 반환")
    void duplicateUserSaveTest() {
        //given
        User user = User.builder()
                .nickName("test")
                .email("test1234@gmail.com")
                .build();

        User saveUser = userRepository.save(user);

        //when
        Long userId = userService.saveUser(user);

        //then
        assertThat(saveUser.getId()).isEqualTo(userId);
    }

}