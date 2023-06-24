package com.ottention.banana.controller;

import com.ottention.banana.config.Login;
import com.ottention.banana.dto.request.LoginUser;
import com.ottention.banana.dto.response.businesscard.StoredCardResponse;
import com.ottention.banana.service.CategoryService;
import com.ottention.banana.service.StoredBusinessCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequiredArgsConstructor
@RequestMapping("/banana/wallet")
public class WalletController {

    private final StoredBusinessCardService storedBusinessCardService;
    private final CategoryService categoryService;


    //전체명함
    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getAllStoredCards(@Login LoginUser user) {
        Map<String, Object> result = new HashMap<>();
        result.put("twoBookmarkedStoredCards", storedBusinessCardService.getTwoBookmarkedCards(user.getId()));
        result.put("allStoredCards", storedBusinessCardService.getAllStoredBusinessCards(user.getId()));

        return ResponseEntity.ok(result);
    }
}
