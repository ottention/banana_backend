package com.ottention.banana.dto.response.businesscard;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
public class CategoryResponse {
    private Long id;
    private String categoryName;
}
