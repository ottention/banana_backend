package com.ottention.banana.api;

import com.ottention.banana.config.SocialLoginConfig;
import com.ottention.banana.response.google.GoogleOAuth2UserInfo;
import com.ottention.banana.response.google.GoogleOAuthResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.reactive.function.BodyInserters;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleAuthApi {

    private final SocialLoginConfig socialLoginConfig;

    public GoogleOAuth2UserInfo getMemberInfo(String idToken) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("id_token", idToken);

        GoogleOAuthResponse userInfo = socialLoginConfig.google().post()
                .uri("/tokeninfo")
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(GoogleOAuthResponse.class)
                .onErrorMap(e -> {
                    log.error("구글 사용자 정보 조회에 실패하였습니다.", e);
                    return new HttpServerErrorException(INTERNAL_SERVER_ERROR, "구글 사용자 정보 조회에 실패하였습니다.");
                })
                .block();

        return new GoogleOAuth2UserInfo(userInfo.getName(), userInfo.getEmail());
    }
}
