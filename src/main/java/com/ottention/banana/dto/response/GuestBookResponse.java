package com.ottention.banana.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ottention.banana.entity.GuestBook;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GuestBookResponse {

    private final Long guestBookId;
    private final String writer;
    private final String content;
    private final Boolean isGuestBookLike;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private final LocalDateTime localDateTime;

    public static GuestBookResponse guestBookResponse(GuestBook guestBook) {
        return GuestBookResponse.builder()
                .guestBookId(guestBook.getId())
                .content(guestBook.getContent())
                .writer(guestBook.getWriter())
                .isGuestBookLike(guestBook.getGuestBookLike())
                .localDateTime(guestBook.getCreatedDate())
                .build();
    }

    @Builder
    public GuestBookResponse(Long guestBookId, String writer, String content, Boolean isGuestBookLike, LocalDateTime localDateTime) {
        this.guestBookId = guestBookId;
        this.writer = writer;
        this.content = content;
        this.isGuestBookLike = isGuestBookLike;
        this.localDateTime = localDateTime;
    }
}
