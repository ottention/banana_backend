package com.ottention.banana.controller;

import com.ottention.banana.config.Login;
import com.ottention.banana.dto.request.LoginUser;
import com.ottention.banana.dto.response.businesscard.CategoryResponse;
import com.ottention.banana.dto.response.businesscard.StoredBusinessCardResponse;
import com.ottention.banana.service.CategoryService;
import com.ottention.banana.service.wallet.StoredBusinessCardService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("/banana/wallet")
public class WalletController {

    StoredBusinessCardService storedBusinessCardService;
    CategoryService categoryService;

    //북마크된 명함 두 개
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/two-bookmarked-cards")
    public List<StoredBusinessCardResponse> getTwoBookmarkedCards(@Login LoginUser user) {
        return storedBusinessCardService.getTwoBookmarkedCards(user.getId());
    }

    //전체명함
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("")
    public List<StoredBusinessCardResponse> getAllStoredCards(@Login LoginUser user, @PageableDefault(sort = "modifiedDate", direction = DESC) Pageable pageable) {
        return storedBusinessCardService.getAllStoredBusinessCards(user.getId(), pageable);
    }

    //북마크
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/bookmark")
    public List<StoredBusinessCardResponse> getBookmarkedStoredBusinessCards(@Login LoginUser user, @PageableDefault(sort = "modifiedDate", direction = DESC) Pageable pageable) {
        return storedBusinessCardService.getBookmarkedStoredCards(user.getId(), pageable);
    }

    //저장 폴더
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/categories")
    public List<CategoryResponse> getStoredCardCategories(@Login LoginUser user) {
        return categoryService.getCategories(user.getId());
    }

    //카테고리 - 상세
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<Map<String, Object>> getStoredCardByCategory(@PathVariable long categoryId, @PageableDefault(sort = "modifiedDate", direction = DESC) Pageable pageable) {
        Map<String, Object> result = new HashMap<>();
        result.put("storedCards", storedBusinessCardService.getStoredCardByCategory(categoryId, pageable));
        result.put("categoryName", categoryService.getCategoryName(categoryId));
        return ResponseEntity.ok(result);
    }
}
