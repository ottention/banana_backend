package com.ottention.banana.service;

import com.ottention.banana.repository.TagRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChartService {

    private final TagRepositoryImpl tagRepository;

    public List<String> findTagsUsedMoreThanTenTimes() {
        return tagRepository.findTagsUsedMoreThanTenTimes();
    }

}
