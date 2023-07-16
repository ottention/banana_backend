package com.ottention.banana.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SaveGuestBookRequest {

    @Length(max = 24, message = "방명록은 최대 24자까지 작성 가능합니다.")
    private String content;

}
