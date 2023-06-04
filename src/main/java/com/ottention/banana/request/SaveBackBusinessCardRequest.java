package com.ottention.banana.request;

import com.ottention.banana.response.businesscard.BusinessCardContentDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class SaveBackBusinessCardRequest {
    private List<BusinessCardContentDto> contents;
}
