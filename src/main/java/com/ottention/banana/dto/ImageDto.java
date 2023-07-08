package com.ottention.banana.dto;

import com.ottention.banana.entity.Coordinate;
import com.ottention.banana.entity.Image;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class ImageDto {

    private Boolean isFront;
    private String imageUrl;
    private Coordinate coordinate;

    public ImageDto(Image image) {
        this.isFront = image.getIsFront();
        this.imageUrl = image.getImageUrl();
        this.coordinate = image.getCoordinate();
    }

}
