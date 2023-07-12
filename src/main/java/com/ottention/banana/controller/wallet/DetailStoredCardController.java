package com.ottention.banana.controller.wallet;

import com.ottention.banana.dto.response.businesscard.wallet.StoredCardDetailResponse;
import com.ottention.banana.service.wallet.DetailStoredBusinessCardService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("banana/wallet")
public class DetailStoredCardController {

    DetailStoredBusinessCardService detailStoredCardService;

    //보관함 명함 (상세)
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public StoredCardDetailResponse get(@PathVariable Long id) {
        return detailStoredCardService.findStoredCardDetail(id);
    }

    //저장 명함 삭제
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        detailStoredCardService.delete(id);
    }
}
