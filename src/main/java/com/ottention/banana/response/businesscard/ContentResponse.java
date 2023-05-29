package com.ottention.banana.response.businesscard;

import com.ottention.banana.entity.ContentSize;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ContentResponse {

    private String content;
    private String contentSize;
    private int xAxis;
    private int yAxis;

    public static ContentResponse toContent(String content, ContentSize contentSize, int xAxis, int yAxis) {
        return ContentResponse.builder()
                .content(content)
                .contentSize(contentSize.getSize())
                .xAxis(xAxis)
                .yAxis(yAxis)
                .build();
    }

    @Builder
    public ContentResponse(String content, String contentSize, int xAxis, int yAxis) {
        this.content = content;
        this.contentSize = contentSize;
        this.xAxis = xAxis;
        this.yAxis = yAxis;
    }

    public String getContent() {
        return content;
    }

    public String getContentSize() {
        return contentSize;
    }

    public int getxAxis() {
        return xAxis;
    }

    public int getyAxis() {
        return yAxis;
    }
}
