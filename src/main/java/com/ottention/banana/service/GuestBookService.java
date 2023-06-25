package com.ottention.banana.service;

import com.ottention.banana.dto.request.SaveGuestBookRequest;
import com.ottention.banana.dto.response.GuestBookResponse;
import com.ottention.banana.entity.BusinessCard;
import com.ottention.banana.entity.GuestBook;
import com.ottention.banana.entity.User;
import com.ottention.banana.exception.BusinessCardNotFound;
import com.ottention.banana.exception.SelfGuestbookNotAllowedException;
import com.ottention.banana.exception.UserNotFound;
import com.ottention.banana.repository.BusinessCardRepository;
import com.ottention.banana.repository.GuestBookRepository;
import com.ottention.banana.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GuestBookService {

    private final BusinessCardRepository businessCardRepository;
    private final GuestBookRepository guestBookRepository;
    private final UserRepository userRepository;

    /**
     *
     * @param userId : 방명록 작성자 id
     * @param businessCardId : 명함 id
     * @param request : 방명록 내용
     */
    @Transactional
    public Long saveGuestBook(Long userId, Long businessCardId, SaveGuestBookRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFound::new);

        BusinessCard businessCard = businessCardRepository.findById(businessCardId)
                .orElseThrow(BusinessCardNotFound::new);

        //자기 자신의 명함엔 방명록을 남길 수 없음
        if (user.getId().equals(businessCard.getUser().getId())) {
            throw new SelfGuestbookNotAllowedException();
        }

        GuestBook guestBook = GuestBook.builder()
                .businessCard(businessCard)
                .user(user)
                .guestBookLike(false)
                .content(request.getContent())
                .writer(user.getNickName())
                .build();

        return guestBookRepository.save(guestBook).getId();
    }

    //자신의 명함의 방명록
    public List<GuestBookResponse> getMyGuestBooks(Long businessCardId, Pageable pageable) {
        List<GuestBook> guestBooks = guestBookRepository.findByBusinessCardId(businessCardId, pageable);
        return guestBooks.stream()
                .map(g -> GuestBookResponse.guestBookResponse(g))
                .collect(Collectors.toList());
    }

    //자신이 작성한 방명록
    public List<GuestBookResponse> getMyWrittenGuestBooks(Long userId, Pageable pageable) {
        List<GuestBook> guestBooks = guestBookRepository.findByUserId(userId, pageable);
        return guestBooks.stream()
                .map(g -> GuestBookResponse.guestBookResponse(g))
                .collect(Collectors.toList());
    }

}
