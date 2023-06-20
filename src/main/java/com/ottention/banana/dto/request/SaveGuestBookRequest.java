package com.ottention.banana.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SaveGuestBookRequest {

    @Length(max = 30, message = "방명록 글자 수는 공백포함 30을 넘길 수 없습니다.")
    private String content;

}
