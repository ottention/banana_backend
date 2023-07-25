package com.ottention.banana.controller;

import com.ottention.banana.config.Login;
import com.ottention.banana.dto.request.LoginUser;
import com.ottention.banana.dto.request.SaveBusinessCardRequest;
import com.ottention.banana.dto.response.businesscard.BusinessCardIdResponse;
import com.ottention.banana.dto.response.businesscard.BusinessCardResponse;
import com.ottention.banana.service.BusinessCardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BusinessCardController {

    private final BusinessCardService businessCardService;

    @GetMapping("/businessCard/home")
    public List<BusinessCardIdResponse> home(@Login LoginUser loginUser) {
        return businessCardService.getBusinessCardIds(loginUser.getId());
    }

    @PostMapping("/businessCard/save")
    public BusinessCardIdResponse saveBusinessCard(@Login LoginUser user, @RequestBody SaveBusinessCardRequest request) {
        Long businessCardId = businessCardService.saveBusinessCard(user.getId(), request);
        return new BusinessCardIdResponse(businessCardId);
    }

    @PatchMapping("/businessCard/{businessCardId}/update")
    public void updateBusinessCard(@Login LoginUser user, @PathVariable Long businessCardId, @RequestBody SaveBusinessCardRequest request) {
        businessCardService.updateBusinessCard(user.getId(), businessCardId, request);
    }

    @GetMapping("/businessCard/{businessCardId}")
    public BusinessCardResponse getBusinessCard(@Login LoginUser user, @PathVariable Long businessCardId) {
        return businessCardService.getBusinessCard(user.getId(), businessCardId);
    }

    @DeleteMapping("/businessCard/{businessCardId}/delete")
    public void deleteBusinessCard(@PathVariable Long businessCardId) {
        businessCardService.deleteBusinessCard(businessCardId);
    }

}
