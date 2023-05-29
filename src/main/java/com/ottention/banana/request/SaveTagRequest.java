package com.ottention.banana.request;

import lombok.Getter;

import java.util.List;

@Getter
public class SaveTagRequest {
    private List<String> tags;
}
