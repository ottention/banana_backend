package com.ottention.banana.controller;

import com.ottention.banana.dto.response.businesscard.BusinessCardResponse;
import com.ottention.banana.dto.response.businesscard.NoteResponse;
import com.ottention.banana.dto.response.businesscard.StoredBusinessCardResponse;
import com.ottention.banana.service.BusinessCardService;
import com.ottention.banana.service.wallet.DetailStoredBusinessCardService;
import com.ottention.banana.service.wallet.NoteService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("banana/wallet")
public class DetailStoredCardController {

    DetailStoredBusinessCardService detailStoredCardService;
    NoteService noteService;

    //보관함 명함 (상세 - 앞)
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{storedCardId}/front")
    public StoredBusinessCardResponse getFront(
            @PathVariable Long storedCardId,
            @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return detailStoredCardService.findStoredBusinessCard(storedCardId, true);
    }

    //보관함 명함 (상세 - 뒤)
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{storedCardId}/back")
    public StoredBusinessCardResponse getBack(
            @PathVariable Long storedCardId,
            @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return detailStoredCardService.findStoredBusinessCard(storedCardId, false);
    }

    //작성노트
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{storedCardId}/notes")
    public List<NoteResponse> getNote(
            @PathVariable Long storedCardId,
            @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return noteService.findAll(storedCardId, pageable);
    }
}
