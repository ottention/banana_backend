package com.ottention.banana.service;

import com.ottention.banana.entity.User;
import com.ottention.banana.repository.UserRepository;
import com.ottention.banana.response.google.GoogleOAuth2UserInfo;
import com.ottention.banana.response.kakao.GetMemberInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Long saveUser(User user) {
        return userRepository.findByEmail(user.getEmail())
                .map(User::getId)
                .orElseGet(() -> userRepository.save(user).getId());
    }

}
