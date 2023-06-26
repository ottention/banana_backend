package com.ottention.banana.repository;

import java.util.List;

public interface TagRepositoryCustom {
    List<String> findTagsUsedMoreThanTenTimes();
}
