package com.ottention.banana.service;

import com.ottention.banana.dto.response.businesscard.BusinessCardLikeResponse;
import com.ottention.banana.entity.BusinessCard;
import com.ottention.banana.entity.User;
import com.ottention.banana.exception.DuplicationLikeException;
import com.ottention.banana.exception.ZeroLikesError;
import com.ottention.banana.repository.BusinessCardRepository;
import com.ottention.banana.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
class BusinessCardLikeServiceTest {

    @Autowired
    private BusinessCardLikeService businessCardLikeService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BusinessCardRepository businessCardRepository;

    @Test
    @DisplayName("명함 좋아요 테스트")
    void likeTest() {
        //given
        User user = User.builder()
                .email("a")
                .nickName("A")
                .build();

        userRepository.save(user);

        BusinessCard businessCard = BusinessCard.builder()
                .user(user)
                .isPublic(true)
                .isRepresent(true)
                .build();

        businessCardRepository.save(businessCard);

        //when
        BusinessCardLikeResponse like = businessCardLikeService.like(user.getId(), businessCard.getId());

        //then
        assertThat(like.getLikeCount()).isEqualTo(1);
        assertThat(like.isLike()).isTrue();
    }

    @Test
    @DisplayName("좋아요 중복 테스트")
    void duplicateLikeTest() {
        //given
        User user = User.builder()
                .email("a")
                .nickName("a")
                .build();

        userRepository.save(user);

        BusinessCard businessCard = BusinessCard.builder()
                .user(user)
                .isPublic(true)
                .isRepresent(true)
                .build();

        businessCardRepository.save(businessCard);

        //when
        businessCardLikeService.like(user.getId(), businessCard.getId());

        //then
        assertThatThrownBy(() -> businessCardLikeService.like(user.getId(), businessCard.getId()))
                .isInstanceOf(DuplicationLikeException.class);
    }

    @Test
    @DisplayName("좋아요 취소 테스트")
    void cancelLikeTest() {
        //given
        User user = User.builder()
                .email("a")
                .nickName("a")
                .build();

        userRepository.save(user);

        BusinessCard businessCard = BusinessCard.builder()
                .user(user)
                .isPublic(true)
                .isRepresent(true)
                .build();

        businessCardRepository.save(businessCard);

        businessCardLikeService.like(user.getId(), businessCard.getId());

        //when
        BusinessCardLikeResponse like = businessCardLikeService.cancelLike(user.getId(), businessCard.getId());

        //then
        assertThat(like.getLikeCount()).isEqualTo(0);
        assertThat(like.isLike()).isFalse();
    }

    @Test
    @DisplayName("좋아요 취소 에러 테스트")
    void zeroLikesErrorTest() {
        //given
        User user = User.builder()
                .email("a")
                .nickName("a")
                .build();

        userRepository.save(user);

        BusinessCard businessCard = BusinessCard.builder()
                .user(user)
                .isPublic(true)
                .isRepresent(true)
                .build();

        businessCardRepository.save(businessCard);

        //when
        businessCardLikeService.like(user.getId(), businessCard.getId());
        businessCardLikeService.cancelLike(user.getId(), businessCard.getId());

        //then
        assertThatThrownBy(() -> businessCardLikeService.cancelLike(user.getId(), businessCard.getId()))
                .isInstanceOf(ZeroLikesError.class);
    }

}