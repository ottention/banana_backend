package com.ottention.banana.service.event;

public enum EventUrl {

    BUSINESS_CARD_LIKE_URL("/businessCard/"),
    SAVE_GUESTBOOK_URL_FRONT("/banana/businessCard/"),
    SAVE_GUESTBOOK_URL_BACK("/guestBook");

    private final String eventUrl;

    private EventUrl(String eventUrl) {
        this.eventUrl = eventUrl;
    }

    public String getEventUrl() {
        return eventUrl;
    }

}
