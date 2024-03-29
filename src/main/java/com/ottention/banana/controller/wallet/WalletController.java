package com.ottention.banana.controller.wallet;

import com.ottention.banana.config.Login;
import com.ottention.banana.dto.request.LoginUser;
import com.ottention.banana.dto.response.businesscard.CategoryCardsResponse;
import com.ottention.banana.dto.response.businesscard.CategoryResponse;
import com.ottention.banana.dto.response.businesscard.wallet.StoredCardDetailResponse;
import com.ottention.banana.dto.response.businesscard.wallet.StoredCardPreviewResponse;
import com.ottention.banana.service.CategoryService;
import com.ottention.banana.service.wallet.StoredBusinessCardService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<StoredCardPreviewResponse> getTwoBookmarkedCards(@Login LoginUser user) {
        return storedBusinessCardService.getTwoBookmarkedCards(user.getId());
    }

    //전체명함
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("")
    public List<StoredCardPreviewResponse> getAllStoredCards(@Login LoginUser user, @PageableDefault(sort = "modifiedDate", direction = DESC) Pageable pageable) {
        return storedBusinessCardService.getAllStoredBusinessCards(user.getId(), pageable);
    }

    //북마크
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/bookmark")
    public List<StoredCardPreviewResponse> getBookmarkedStoredBusinessCards(@Login LoginUser user, @PageableDefault(sort = "modifiedDate", direction = DESC) Pageable pageable) {
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
    public CategoryCardsResponse getStoredCardByCategory(@PathVariable Long categoryId, @PageableDefault(sort = "modifiedDate", direction = DESC) Pageable pageable) {
        return categoryService.getCategoryCards(categoryId, pageable);
    }
}
