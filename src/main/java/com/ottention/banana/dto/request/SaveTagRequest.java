package com.ottention.banana.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class SaveTagRequest {
    private List<String> tags;
}
