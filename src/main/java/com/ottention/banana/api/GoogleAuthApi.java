package com.ottention.banana.api;

import com.ottention.banana.config.AppConfig;
import com.ottention.banana.response.google.GoogleOAuth2UserInfo;
import com.ottention.banana.response.google.GoogleOAuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class GoogleAuthApi {

    private final AppConfig appConfig;

    public GoogleOAuth2UserInfo getMemberInfo(String idToken) {
        WebClient webClient = appConfig.google();

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("id_token", idToken);

        GoogleOAuthResponse userInfo = webClient.post()
                .uri("/tokeninfo")
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(GoogleOAuthResponse.class)
                .block();

        return new GoogleOAuth2UserInfo(userInfo.getName(), userInfo.getEmail());
    }
}
