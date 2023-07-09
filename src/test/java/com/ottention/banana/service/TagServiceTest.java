package com.ottention.banana.service;

import com.ottention.banana.entity.BusinessCard;
import com.ottention.banana.entity.User;
import com.ottention.banana.exception.TagLimitExceededException;
import com.ottention.banana.repository.BusinessCardRepository;
import com.ottention.banana.repository.TagRepository;
import com.ottention.banana.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
class TagServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BusinessCardRepository businessCardRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TagService tagService;

    @Test
    @DisplayName("태그 저장 테스트")
    void saveTagTest() {
        //given
        User user = User.builder()
                .nickName("a")
                .email("a")
                .build();

        userRepository.save(user);

        BusinessCard businessCard = BusinessCard.builder()
                .isRepresent(true)
                .isPublic(true)
                .user(user)
                .build();

        businessCardRepository.save(businessCard);

        List<String> tags = new ArrayList<>();
        tags.add("잇타");
        tags.add("오텐션 바나나");

        //when
        tagService.saveTag(tags, businessCard);

        //then
        assertThat(tagRepository).isNotNull();
    }

    @Test
    @DisplayName("태그를 10개 이상 저장 시 예외 테스트")
    void tagLimitExceededExceptionTest() {
        //given
        User user = User.builder()
                .nickName("a")
                .email("a")
                .build();

        userRepository.save(user);

        BusinessCard businessCard = BusinessCard.builder()
                .isRepresent(true)
                .isPublic(true)
                .user(user)
                .build();

        businessCardRepository.save(businessCard);

        List<String> tags = new ArrayList<>();

        //when
        for (int i = 0; i < 20; i++) {
            tags.add("태그테스트" + i);
        }

        //then
        assertThatThrownBy(() -> tagService.saveTag(tags, businessCard))
                .isInstanceOf(TagLimitExceededException.class);
    }

}