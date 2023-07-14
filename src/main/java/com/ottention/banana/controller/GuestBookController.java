package com.ottention.banana.controller;

import com.ottention.banana.config.Login;
import com.ottention.banana.dto.request.LoginUser;
import com.ottention.banana.dto.request.SaveGuestBookRequest;
import com.ottention.banana.dto.response.GuestBookLikeResponse;
import com.ottention.banana.dto.response.GuestBookResponse;
import com.ottention.banana.service.GuestBookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequiredArgsConstructor
public class GuestBookController {

    private final GuestBookService guestBookService;

    @PostMapping("/banana/businessCard/{businessCardId}/writeGuestBook")
    public Long saveGuestBook(@Login LoginUser user, @PathVariable Long businessCardId,
                              @RequestBody @Valid SaveGuestBookRequest request) {
        return guestBookService.saveGuestBook(user.getId(), businessCardId, request);
    }

    @GetMapping("/banana/businessCard/{businessCardId}/guestBook")
    public List<GuestBookResponse> getBusinessCardGuestBook(@PathVariable Long businessCardId, @PageableDefault(sort = "id", direction = DESC) Pageable pageable) {
        return guestBookService.getMyGuestBooks(businessCardId, pageable);
    }

    @GetMapping("/banana/myWrittenGuestBooks")
    public List<GuestBookResponse> getMyGuestBook(@Login LoginUser user, @PageableDefault(sort = "id", direction = DESC) Pageable pageable) {
        return guestBookService.getMyWrittenGuestBooks(user.getId(), pageable);
    }

    @PatchMapping("/businessCard/myGuestBook/{guestBookId}/edit")
    public void updateGuestBook(@Login LoginUser user, @PathVariable Long guestBookId,
                                @RequestBody @Valid SaveGuestBookRequest request) {
        guestBookService.updateGuestBook(user.getId(), guestBookId, request);
    }

    @DeleteMapping("/businessCard/myGuestBook/{guestBookId}/delete")
    public void deleteMyWrittenGuestBook(@Login LoginUser user, @PathVariable Long guestBookId) {
        guestBookService.deleteMyWrittenGuestBook(user.getId(), guestBookId);
    }

    @DeleteMapping("/businessCard/{businessCardId}/guestBook/{guestBookId}/delete")
    public void deleteMyBusinessCardGuestBook(@PathVariable Long businessCardId, @PathVariable Long guestBookId) {
        guestBookService.deleteMyBusinessCardGuestBook(businessCardId, guestBookId);
    }

    @PostMapping("/businessCard/{businessCardId}/guestBook/{guestBookId}/like")
    public GuestBookLikeResponse guestBookLike(@PathVariable Long businessCardId, @PathVariable Long guestBookId) {
        return guestBookService.guestBookLike(businessCardId, guestBookId);
    }

    @PostMapping("/businessCard/{businessCardId}/guestBook/{guestBookId}/cancelLike")
    public GuestBookLikeResponse guestBookCancelLike(@PathVariable Long businessCardId, @PathVariable Long guestBookId) {
        return guestBookService.cancelGuestBookLike(businessCardId, guestBookId);
    }

}
