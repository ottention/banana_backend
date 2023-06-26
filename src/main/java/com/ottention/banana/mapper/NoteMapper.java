package com.ottention.banana.mapper;

import com.ottention.banana.dto.response.businesscard.NoteResponse;
import com.ottention.banana.entity.Note;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NoteMapper {
    NoteResponse toResponse(Note entity);
}
