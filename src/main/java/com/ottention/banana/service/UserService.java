package com.ottention.banana.service;

import com.ottention.banana.entity.User;
import com.ottention.banana.repository.UserRepository;
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

        return userRepository.save(user).getId();
    }

}
