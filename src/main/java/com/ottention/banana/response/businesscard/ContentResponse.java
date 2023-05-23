package com.ottention.banana.response.businesscard;

import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ContentResponse {

    private String content;
    private int width;
    private int height;
    private int xAxis;
    private int yAxis;

    public static ContentResponse toContent(String content, int width, int height, int xAxis, int yAxis) {
        return ContentResponse.builder()
                .content(content)
                .width(width)
                .height(height)
                .xAxis(xAxis)
                .yAxis(yAxis)
                .build();
    }

    @Builder
    public ContentResponse(String content, int width, int height, int xAxis, int yAxis) {
        this.content = content;
        this.width = width;
        this.height = height;
        this.xAxis = xAxis;
        this.yAxis = yAxis;
    }

    public String getContent() {
        return content;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getxAxis() {
        return xAxis;
    }

    public int getyAxis() {
        return yAxis;
    }
}
