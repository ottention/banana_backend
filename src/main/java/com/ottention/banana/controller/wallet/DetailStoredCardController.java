package com.ottention.banana.controller.wallet;

import com.ottention.banana.dto.response.businesscard.StoredBusinessCardResponse;
import com.ottention.banana.service.wallet.DetailStoredBusinessCardService;
import com.ottention.banana.service.wallet.NoteService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("banana/wallet")
public class DetailStoredCardController {

    DetailStoredBusinessCardService detailStoredCardService;
    NoteService noteService;

    //보관함 명함 (상세 - 앞)
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}/front")
    public StoredBusinessCardResponse getFront(
            @PathVariable Long id,
            @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return detailStoredCardService.findStoredBusinessCard(id, true);
    }

    //보관함 명함 (상세 - 뒤)
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}/back")
    public StoredBusinessCardResponse getBack(
            @PathVariable Long id,
            @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return detailStoredCardService.findStoredBusinessCard(id, false);
    }

    //저장 명함 삭제
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        detailStoredCardService.delete(id);
    }
}
