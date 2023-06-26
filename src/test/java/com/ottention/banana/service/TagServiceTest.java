package com.ottention.banana.service;

import com.ottention.banana.dto.request.SaveTagRequest;
import com.ottention.banana.dto.response.businesscard.TagResponse;
import com.ottention.banana.entity.BusinessCard;
import com.ottention.banana.entity.Tag;
import com.ottention.banana.entity.User;
import com.ottention.banana.exception.TagLimitExceededException;
import com.ottention.banana.repository.BusinessCardRepository;
import com.ottention.banana.repository.TagRepository;
import com.ottention.banana.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

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

    @BeforeEach
    void clean() {
        businessCardRepository.deleteAll();
        tagRepository.deleteAll();
        userRepository.deleteAll();
    }

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

        SaveTagRequest request = new SaveTagRequest();
        List<String> tags = new ArrayList<>();
        tags.add("잇타");
        tags.add("오텐션 바나나");
        request.setTags(tags);

        //when
        tagService.saveTag(request, businessCard);
        List<Tag> tagList = tagRepository.findAll();

        //then
        assertThat(tagList.get(0).getName()).isEqualTo("잇타");
        assertThat(tagList.get(1).getName()).isEqualTo("오텐션바나나");
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

        SaveTagRequest request = new SaveTagRequest();
        List<String> tags = new ArrayList<>();

        //when
        for (int i = 0; i < 20; i++) {
            tags.add("태그테스트" + i);
        }
        request.setTags(tags);

        //then
        assertThatThrownBy(() -> tagService.saveTag(request, businessCard))
                .isInstanceOf(TagLimitExceededException.class);
    }

    @Test
    @DisplayName("태그 조회 테스트")
    void getTagsTest() {
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

        SaveTagRequest request = new SaveTagRequest();
        List<String> tags = new ArrayList<>();
        tags.add("잇타");
        tags.add("오텐션 바나나");
        request.setTags(tags);

        tagService.saveTag(request, businessCard);

        //when
        List<TagResponse> tagList = tagService.getTags(businessCard.getId());

        //then
        assertThat(tagList.get(0).getTag()).isEqualTo("잇타");
        assertThat(tagList.get(1).getTag()).isEqualTo("오텐션바나나");
    }

}