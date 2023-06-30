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

    //작성노트
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}/notes")
    public List<NoteResponse> getNote(
            @PathVariable Long id,
            @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return noteService.findAll(id, pageable);
    }

    //저장 명함 삭제
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        detailStoredCardService.delete(id);
    }
}
