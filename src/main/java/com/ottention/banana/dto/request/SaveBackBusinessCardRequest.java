package com.ottention.banana.dto.request;

import com.ottention.banana.dto.BusinessCardContentDto;
import com.ottention.banana.entity.Coordinate;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class SaveBackBusinessCardRequest {
    private List<BusinessCardContentDto> contents;
    private List<Coordinate> backImageCoordinates;
}
