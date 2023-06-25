package com.ottention.banana.controller;

import com.ottention.banana.service.wallet.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/banana/wallet")
public class BookmarkController {

    private final BookmarkService bookmarkService;


    //즐겨찾기 등록
    @PostMapping("/categories/{categoryId}/bookmark/{cardId}")
    public ResponseEntity<Void> patch(@PathVariable Long cardId) {
        bookmarkService.modifyIsBookmarked(cardId);
        return ResponseEntity.ok().build();
    }
}
