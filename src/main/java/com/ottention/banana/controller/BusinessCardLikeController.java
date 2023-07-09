package com.ottention.banana.controller;

import com.ottention.banana.config.Login;
import com.ottention.banana.dto.request.LoginUser;
import com.ottention.banana.dto.response.businesscard.BusinessCardLikeResponse;
import com.ottention.banana.service.BusinessCardLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BusinessCardLikeController {

    private final BusinessCardLikeService businessCardLikeService;

    @PostMapping("/businessCard/{businessCardId}/like")
    public BusinessCardLikeResponse like(@Login LoginUser loginUser, @PathVariable Long businessCardId) {
        return businessCardLikeService.like(loginUser.getId(), businessCardId);
    }

    @DeleteMapping("/businessCard/{businessCardId}/cancelLike")
    public BusinessCardLikeResponse cancelLike(@Login LoginUser loginUser, @PathVariable Long businessCardId) {
        return businessCardLikeService.cancelLike(loginUser.getId(), businessCardId);
    }

}
