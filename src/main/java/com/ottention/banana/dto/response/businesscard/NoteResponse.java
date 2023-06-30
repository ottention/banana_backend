package com.ottention.banana.dto.response.businesscard;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
public class NoteResponse {
    private Long id;
    private String content;
}
