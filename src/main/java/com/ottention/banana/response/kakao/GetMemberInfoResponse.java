package com.ottention.banana.response.kakao;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

/**
 * kakaoAccount는 JSON으로 직렬화될 때 kakaoAccount로 표현 -> 카멜 케이스와 스네이크 케이스 간의 네이밍 규칙 불일치
 * -> Jackson은 해당 프로퍼티를 찾지 못하고 null로 처리
 * @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class) 어노테이션을 사용하여 프로퍼티 이름을 스네이크 케이스로 맞춰주면
 * JSON 직렬화/역직렬화 과정에서 올바르게 매핑되어 null이 발생 x
 */
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GetMemberInfoResponse {
    private KakaoAccount kakaoAccount;
}
