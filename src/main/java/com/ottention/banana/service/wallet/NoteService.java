package com.ottention.banana.service.wallet;

import com.ottention.banana.dto.request.SaveNoteRequest;
import com.ottention.banana.dto.response.businesscard.NoteResponse;
import com.ottention.banana.entity.wallet.Note;
import com.ottention.banana.mapper.NoteMapper;
import com.ottention.banana.repository.wallet.NoteRepository;
import com.ottention.banana.repository.wallet.StoredBusinessCardRepository;
import jakarta.persistence.EntityNotFoundException;
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
    private final StoredBusinessCardRepository storedCardRepository;
    private final NoteMapper noteMapper;

    public List<NoteResponse> findAll(Long param, Pageable pageable) {
        List<Note> notes = noteRepository.findAllByStoredBusinessCardId(param, pageable);
        return notes.stream().map(noteMapper::toResponse).toList();
    }

    @Transactional
    public void create(Long id, SaveNoteRequest request) {
        noteRepository.save(noteMapper.toEntity(request.getContent(), storedCardRepository.findById(id).orElseThrow(EntityNotFoundException::new)));
    }

    @Transactional
    public void delete(Long noteId) {
        noteRepository.delete(noteRepository.findById(noteId).orElseThrow(EntityNotFoundException::new));
    }
}
