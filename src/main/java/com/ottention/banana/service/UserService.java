package com.ottention.banana.service;

import com.ottention.banana.dto.response.google.GoogleOAuth2UserInfo;
import com.ottention.banana.dto.response.kakao.GetMemberInfoResponse;
import com.ottention.banana.entity.User;
import com.ottention.banana.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Long saveFromKakao(GetMemberInfoResponse userInfo) {
        User user = User.builder()
                .nickName(userInfo.getKakaoAccount().getProfile().getNickname())
                .email(userInfo.getKakaoAccount().getEmail())
                .build();
        return saveUser(user);
    }

    public Long saveFromGoogle(GoogleOAuth2UserInfo userInfo) {
        User user = User.builder()
                .nickName(userInfo.getName())
                .email(userInfo.getEmail())
                .build();
        return saveUser(user);
    }

    private Long saveUser(User user) {
        log.info("email = {}", user.getEmail());
        return userRepository.findByEmail(user.getEmail())
                .map(User::getId)
                .orElseGet(() -> userRepository.save(user).getId());
    }

}
