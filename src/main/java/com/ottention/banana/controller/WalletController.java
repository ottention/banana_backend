package com.ottention.banana.controller;

import com.ottention.banana.config.Login;
import com.ottention.banana.dto.request.LoginUser;
import com.ottention.banana.dto.response.businesscard.StoredCardResponse;
import com.ottention.banana.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/banana/wallet")
public class WalletController {

    private final WalletService walletService;

    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getAllStoredCards(@Login LoginUser user) {
        Map<String, Object> result = new HashMap<>();
        result.put("twoBookmarkedStoredCards", walletService.getTwoBookmarkedCards(user.getId()));
        result.put("allStoredCards", walletService.getAllStoredBusinessCards(user.getId()));

        return ResponseEntity.ok(result);
    }

}
