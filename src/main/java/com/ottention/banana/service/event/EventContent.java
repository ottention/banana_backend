package com.ottention.banana.service.event;

public enum EventContent {
    BUSINESS_CARD_LIKE_CONTENT("명함에 좋아요가 눌렸습니다."),
    SAVE_GUESTBOOK_CONTENT("님이 방명록을 남겼습니다.");

    private final String eventContent;

    private EventContent(String eventContent) {
        this.eventContent = eventContent;
    }

    public String getEventContent() {
        return eventContent;
    }
}
