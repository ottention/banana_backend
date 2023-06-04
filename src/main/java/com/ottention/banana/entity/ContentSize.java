package com.ottention.banana.entity;

import com.ottention.banana.exception.InvalidContentSize;

public enum ContentSize {

    H1("h1"), H2("h2");

    private String size;

    ContentSize(String size) {
        this.size = size;
    }

    public String getSize() {
        return size;
    }

    public static ContentSize fromString(String value) {
        for (ContentSize size : ContentSize.values()) {
            if (size.getSize().equalsIgnoreCase(value)) {
                return size;
            }
        }
        throw new InvalidContentSize();
    }

}
