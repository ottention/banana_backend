package com.ottention.banana.controller.wallet;

import com.ottention.banana.dto.request.notification.SaveNoteRequest;
import com.ottention.banana.dto.response.businesscard.NoteResponse;
import com.ottention.banana.service.wallet.NoteService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("banana/wallet")
public class NoteController {

    NoteService noteService;

    //작성노트 조회
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}/notes")
    public List<NoteResponse> get(
            @PathVariable Long id,
            @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return noteService.findAll(id, pageable);
    }

    //노트 작성
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{id}/notes")
    public void create(@PathVariable Long id, @RequestBody @Valid SaveNoteRequest request) {
        noteService.create(id, request);
    }

    //노트 삭제
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}/notes/{noteId}")
    public void delete(@PathVariable Long noteId) {
        noteService.delete(noteId);
    }
}
