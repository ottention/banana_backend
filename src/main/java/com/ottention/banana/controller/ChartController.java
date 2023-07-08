package com.ottention.banana.controller;

import com.ottention.banana.config.Login;
import com.ottention.banana.dto.request.LoginUser;
import com.ottention.banana.dto.response.businesscard.BusinessCardResponse;
import com.ottention.banana.dto.response.businesscard.TagResponse;
import com.ottention.banana.service.ChartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChartController {

    private final ChartService chartService;

    @GetMapping("/businessCard/topTenTags")
    public List<TagResponse> getTopTenTags() {
        return chartService.getTopTenTags();
    }

    @GetMapping("/businessCard/topTenBusinessCards")
    public List<BusinessCardResponse> getTopTensBusinessCards(@Login LoginUser loginUser) {
        return chartService.getTopTenBusinessCards(loginUser.getId());
    }

}
