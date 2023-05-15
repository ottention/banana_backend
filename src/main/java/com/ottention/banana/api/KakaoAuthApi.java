package com.ottention.banana.api;

import com.ottention.banana.config.AppConfig;
import com.ottention.banana.config.KakaoConfig;
import com.ottention.banana.response.kakao.GetMemberInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoAuthApi {

    private final AppConfig appConfig;
    private final KakaoConfig kakaoConfig;

    public GetMemberInfoResponse getMemberInfo(String accessToken) {
        return appConfig.serviceApi().get()
                .uri(kakaoConfig.getGetMemberInfoUrl())
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToFlux(GetMemberInfoResponse.class)
                .onErrorMap(e -> {
                    log.error("카카오 사용자 정보 조회에 실패하였습니다.", e);
                    return new HttpServerErrorException(INTERNAL_SERVER_ERROR, "카카오 사용자 정보 조회에 실패하였습니다.");
                })
                .blockLast();
    }
}
