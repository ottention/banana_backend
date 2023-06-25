package com.ottention.banana.controller;

import com.ottention.banana.dto.response.businesscard.BusinessCardResponse;
import com.ottention.banana.dto.response.businesscard.NoteResponse;
import com.ottention.banana.service.BusinessCardService;
import com.ottention.banana.service.wallet.DetailStoredBusinessCardService;
import com.ottention.banana.service.wallet.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("banana/wallet")
public class DetailStoredCardController {

    private final DetailStoredBusinessCardService detailStoredCardService;
    private final NoteService noteService;

    //보관함 명함 (상세 - 앞)
    @GetMapping("/{storedCardId}/front")
    public ResponseEntity<BusinessCardResponse> getFront(
            @PathVariable Long storedCardId,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(detailStoredCardService.findStoredBusinessCardFront(storedCardId, pageable));
    }

    //보관함 명함 (상세 - 뒤)
    @GetMapping("/{storedCardId}/front")
    public ResponseEntity<BusinessCardResponse> getBack(
            @PathVariable Long storedCardId,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(detailStoredCardService.findStoredBusinessCardFront(storedCardId, pageable));
    }

    //작성노트
    @GetMapping("/{storedCardId}/notes")
    public ResponseEntity<List<NoteResponse>> getNote(
            @PathVariable Long storedCardId,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(noteService.findAll(storedCardId, pageable));
    }

}
