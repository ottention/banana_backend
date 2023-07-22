package com.ottention.banana.controller;

import com.ottention.banana.config.Login;
import com.ottention.banana.dto.request.LoginUser;
import com.ottention.banana.dto.response.businesscard.BusinessCardResponse;
import com.ottention.banana.dto.response.businesscard.TagResponse;
import com.ottention.banana.service.chart.ChartService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public List<BusinessCardResponse> getTopTensBusinessCards(@RequestParam String name, @Login LoginUser loginUser,
                                                              @PageableDefault Pageable pageable) {
        return chartService.getTopTenBusinessCards(name, loginUser.getId(), pageable);
    }

}
