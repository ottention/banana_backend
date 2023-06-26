package com.ottention.banana.controller;

import com.ottention.banana.service.ChartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChartController {

    private final ChartService chartService;

    @GetMapping("/getTags")
    public List<String> getTags() {
        return chartService.findTagsUsedMoreThanTenTimes();
    }

}
