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

    //즐겨찾기
    @GetMapping("/bookmark")
    public ResponseEntity<List<StoredCardResponse>> getBookmarkedStoredBusinessCards(@Login LoginUser user, @PageableDefault(sort = "modifiedDate", direction = DESC) Pageable pageable) {
        List<StoredCardResponse> result = storedBusinessCardService.getBookmarkedStoredCards(user.getId(), pageable);

        return ResponseEntity.ok(result);
    }

    //저장 폴더
    @GetMapping("/categories")
    public ResponseEntity<Map<String, Object>> getStoredCardCategories(@Login LoginUser user) {
        Map<String, Object> result = new HashMap<>();
        result.put("categories", categoryService.getCategories(user.getId()));
        result.put("twoBookmarkedStoredCards", storedBusinessCardService.getTwoBookmarkedCards(user.getId()));

        return ResponseEntity.ok(result);
    }

    //카테고리 - 상세
    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<Map<String, Object>> getStoredCardByCategory(@PathVariable long categoryId, @PageableDefault(sort = "modifiedDate", direction = DESC) Pageable pageable) {
        Map<String, Object> result = new HashMap<>();
        result.put("storedCards", storedBusinessCardService.getStoredCardByCategory(categoryId, pageable));
        result.put("categoryName", categoryService.getCategoryName(categoryId));
        return ResponseEntity.ok(result);
    }
}
