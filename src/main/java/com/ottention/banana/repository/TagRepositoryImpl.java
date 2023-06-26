package com.ottention.banana.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.ottention.banana.entity.QTag.tag;

@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<String> findTagsUsedMoreThanTenTimes() {
        return queryFactory.select(tag.name)
                .from(tag)
                .groupBy(tag.name)
                .having(tag.count().goe(10))
                .fetch();
    }

}
