package com.ottention.banana.request;

import com.ottention.banana.response.businesscard.ContentResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class SaveBackBusinessCardRequest {
    private List<ContentResponse> contents;
}
