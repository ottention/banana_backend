package com.ottention.banana.controller;

import com.ottention.banana.dto.response.jwt.ReissueResponse;
import com.ottention.banana.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JwtController {

    private final JwtService jwtService;

    @PostMapping("/reissue")
    public ReissueResponse reissueToken(@RequestHeader("refreshToken") String refreshToken) {
        String reissuedAccessToken = jwtService.reissueAccessToken(refreshToken);
        return new ReissueResponse(reissuedAccessToken);
    }

}
