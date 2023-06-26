package com.ottention.banana.service.wallet;

import com.ottention.banana.dto.response.businesscard.NoteResponse;
import com.ottention.banana.entity.Note;
import com.ottention.banana.mapper.NoteMapper;
import com.ottention.banana.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NoteService {
    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;

    public List<NoteResponse> findAll(Long param, Pageable pageable) {
        List<Note> notes = noteRepository.findAllByStoredBusinessCardId(param, pageable);
        return notes.stream().map(noteMapper::toResponse).toList();
    }
}
