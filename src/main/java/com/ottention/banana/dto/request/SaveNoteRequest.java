package com.ottention.banana.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SaveNoteRequest {

    @Size(max=120, message = "노트 작성은 공백 포함 30자를 넘길 수 없습니다.")
    private String content;
}
