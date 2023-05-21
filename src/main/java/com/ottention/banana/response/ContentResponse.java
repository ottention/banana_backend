package com.ottention.banana.response;

import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ContentResponse {

    private String content;
    private int xAxis;
    private int yAxis;

    public static ContentResponse toContent(String content, int xAxis, int yAxis) {
        return ContentResponse.builder()
                .content(content)
                .xAxis(xAxis)
                .yAxis(yAxis)
                .build();
    }

    @Builder
    public ContentResponse(String content, int xAxis, int yAxis) {
        this.content = content;
        this.xAxis = xAxis;
        this.yAxis = yAxis;
    }

    public String getContent() {
        return content;
    }

    public int getxAxis() {
        return xAxis;
    }

    public int getyAxis() {
        return yAxis;
    }
}
