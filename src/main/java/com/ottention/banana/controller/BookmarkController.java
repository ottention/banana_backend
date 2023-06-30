package com.ottention.banana.controller;

import com.ottention.banana.service.wallet.BookmarkService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("/banana/wallet")
public class BookmarkController {

    BookmarkService bookmarkService;

    //즐겨찾기 등록 해제
    @PostMapping("/categories/{categoryId}/bookmark/{cardId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void patch(@PathVariable Long cardId, @RequestParam("isBookmarked") Boolean isBookmarked) {
        bookmarkService.modifyBookmark(cardId, isBookmarked);
    }
}
