package com.ottention.banana.service;

import com.ottention.banana.dto.request.SaveGuestBookRequest;
import com.ottention.banana.dto.response.GuestBookResponse;
import com.ottention.banana.entity.BusinessCard;
import com.ottention.banana.entity.GuestBook;
import com.ottention.banana.entity.User;
import com.ottention.banana.exception.InvalidRequest;
import com.ottention.banana.exception.guestBook.SelfGuestbookNotAllowedException;
import com.ottention.banana.repository.BusinessCardRepository;
import com.ottention.banana.repository.GuestBookRepository;
import com.ottention.banana.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Transactional
@SpringBootTest
class GuestBookServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BusinessCardRepository businessCardRepository;

    @Autowired
    GuestBookRepository guestBookRepository;

    @Autowired
    GuestBookService guestBookService;

    @Test
    @DisplayName("방명록 작성 테스트")
    void saveGuestBookTest() {
        //given
        User user1 = User.builder()
                .email("a")
                .nickName("a")
                .build();

        User user2 = User.builder()
                .email("a")
                .nickName("a")
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

        BusinessCard businessCard = BusinessCard.builder()
                .user(user1)
                .isPublic(true)
                .isRepresent(true)
                .build();

        businessCardRepository.save(businessCard);

        SaveGuestBookRequest saveGuestBookRequest = new SaveGuestBookRequest("방명록 내용 테스트");
        Long guestBookId = guestBookService.saveGuestBook(user2.getId(), businessCard.getId(), saveGuestBookRequest);

        //when
        GuestBook guestBook = guestBookRepository.findById(guestBookId)
                .orElseThrow();

        //then
        assertThat(guestBook.getGuestBookLike()).isFalse();
        assertThat(guestBook.getContent()).isEqualTo("방명록 내용 테스트");
        assertThat(guestBook.getUser()).isEqualTo(user2);
        assertThat(guestBook.getBusinessCard()).isEqualTo(businessCard);
    }

    @Test
    @DisplayName("자기 자신의 명함의 방명록 작성 테스트")
    void selfGuestBookNotAllowedExceptionTest() {
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

        SaveGuestBookRequest saveGuestBookRequest = new SaveGuestBookRequest("방명록 내용 테스트");

        assertThatThrownBy(() -> guestBookService.saveGuestBook(user.getId(), businessCard.getId(), saveGuestBookRequest))
                .isInstanceOf(SelfGuestbookNotAllowedException.class);
    }

    @Test
    @DisplayName("명함의 방명록 조회")
    void getBusinessCardGuestBookTest() {
        //given
        User user1 = User.builder()
                .email("a")
                .nickName("a")
                .build();

        User user2 = User.builder()
                .email("a")
                .nickName("a")
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

        BusinessCard businessCard = BusinessCard.builder()
                .user(user1)
                .isPublic(true)
                .isRepresent(true)
                .build();

        businessCardRepository.save(businessCard);

        List<GuestBook> guestBooks = IntStream.range(0, 20).mapToObj(i -> GuestBook.builder()
                        .content("방명록 내용 테스트" + i)
                        .guestBookLike(false)
                        .user(user2)
                        .businessCard(businessCard)
                        .writer(user2.getNickName())
                        .build())
                .collect(Collectors.toList());

        guestBookRepository.saveAll(guestBooks);

        //when
        Pageable pageable = PageRequest.of(0, 10, DESC, "id");
        List<GuestBookResponse> myGuestBooks = guestBookService.getMyGuestBooks(businessCard.getId(), pageable);

        //then
        assertEquals(myGuestBooks.get(0).getContent(), "방명록 내용 테스트19");
        assertEquals(myGuestBooks.get(9).getContent(), "방명록 내용 테스트10");
    }

    @Test
    @DisplayName("자신이 작성한 방명록 조회")
    void getMyWrittenGuestBooks() {
        //given
        User user1 = User.builder()
                .email("a")
                .nickName("a")
                .build();

        User user2 = User.builder()
                .email("a")
                .nickName("a")
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

        BusinessCard businessCard = BusinessCard.builder()
                .user(user1)
                .isPublic(true)
                .isRepresent(true)
                .build();

        businessCardRepository.save(businessCard);

        List<GuestBook> guestBooks = IntStream.range(0, 20).mapToObj(i -> GuestBook.builder()
                        .content("방명록 내용" + i)
                        .guestBookLike(false)
                        .user(user2)
                        .businessCard(businessCard)
                        .writer(user2.getNickName())
                        .build())
                .collect(Collectors.toList());

        guestBookRepository.saveAll(guestBooks);

        //when
        Pageable pageable = PageRequest.of(0, 10, DESC, "id");
        List<GuestBookResponse> myGuestBooks = guestBookService.getMyWrittenGuestBooks(user2.getId(), pageable);

        //then
        assertEquals(myGuestBooks.get(0).getContent(), "방명록 내용19");
        assertEquals(myGuestBooks.get(9).getContent(), "방명록 내용10");
    }

    @Test
    @DisplayName("자기가 작성하지 않은 방명록 삭제 테스트")
    void invalidRequestTest() {
        //given
        User user1 = User.builder()
                .email("a")
                .nickName("a")
                .build();

        User user2 = User.builder()
                .email("a")
                .nickName("a")
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

        BusinessCard businessCard = BusinessCard.builder()
                .user(user1)
                .isPublic(true)
                .isRepresent(true)
                .build();

        businessCardRepository.save(businessCard);

        GuestBook guestBook = GuestBook.builder()
                .content("방명록 내용")
                .guestBookLike(false)
                .user(user2)
                .businessCard(businessCard)
                .writer(user2.getNickName())
                .build();

        guestBookRepository.save(guestBook);

        //then
        assertThatThrownBy(() -> guestBookService.deleteMyWrittenGuestBook(user1.getId(), guestBook.getId()))
                .isInstanceOf(InvalidRequest.class);
    }

    @Test
    @DisplayName("내 명함의 방명록 삭제 테스트")
    void deleteMyBusinessCardGuestBookTest() {
        //given
        User user1 = User.builder()
                .email("a")
                .nickName("a")
                .build();

        User user2 = User.builder()
                .email("a")
                .nickName("a")
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

        BusinessCard businessCard = BusinessCard.builder()
                .user(user1)
                .isPublic(true)
                .isRepresent(true)
                .build();

        businessCardRepository.save(businessCard);

        GuestBook guestBook = GuestBook.builder()
                .content("방명록 내용")
                .guestBookLike(false)
                .user(user2)
                .businessCard(businessCard)
                .writer(user2.getNickName())
                .build();

        guestBookRepository.save(guestBook);

        //when
        guestBookService.deleteMyBusinessCardGuestBook(businessCard.getId(), guestBook.getId());

        //then
        Optional<GuestBook> findGuestBook = guestBookRepository.findById(guestBook.getId());
        assertThat(findGuestBook).isEmpty();
    }

    @Test
    @DisplayName("내가 쓴 방명록 삭제")
    void deleteMyWrittenGuestBookTest() {
        //given
        User user1 = User.builder()
                .email("a")
                .nickName("a")
                .build();

        User user2 = User.builder()
                .email("a")
                .nickName("a")
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

        BusinessCard businessCard = BusinessCard.builder()
                .user(user1)
                .isPublic(true)
                .isRepresent(true)
                .build();

        businessCardRepository.save(businessCard);

        GuestBook guestBook = GuestBook.builder()
                .content("방명록 내용")
                .guestBookLike(false)
                .user(user2)
                .businessCard(businessCard)
                .writer(user2.getNickName())
                .build();

        guestBookRepository.save(guestBook);

        //when
        guestBookService.deleteMyWrittenGuestBook(user2.getId(), guestBook.getId());

        //then
        Optional<GuestBook> findGuestBook = guestBookRepository.findById(guestBook.getId());
        assertThat(findGuestBook).isEmpty();
    }

}