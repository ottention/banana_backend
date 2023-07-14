package com.ottention.banana.service;

import com.ottention.banana.dto.request.SaveGuestBookRequest;
import com.ottention.banana.dto.response.GuestBookLikeResponse;
import com.ottention.banana.dto.response.GuestBookResponse;
import com.ottention.banana.entity.BusinessCard;
import com.ottention.banana.entity.GuestBook;
import com.ottention.banana.entity.User;
import com.ottention.banana.exception.BusinessCardNotFound;
import com.ottention.banana.exception.InvalidRequest;
import com.ottention.banana.exception.guestBook.GuestBookNotFound;
import com.ottention.banana.exception.guestBook.SelfGuestbookNotAllowedException;
import com.ottention.banana.exception.UserNotFound;
import com.ottention.banana.repository.BusinessCardRepository;
import com.ottention.banana.repository.GuestBookRepository;
import com.ottention.banana.repository.UserRepository;
import com.ottention.banana.service.event.EventContent;
import com.ottention.banana.service.event.EventUrl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
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
    private final ApplicationEventPublisher eventPublisher;

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
        //방명록 작성 알림 전송
        notifyGuestBookInfo(businessCard, user);
        return guestBookRepository.save(guestBook).getId();
    }

    //내가 쓴 방명록 수정
    @Transactional
    public void updateGuestBook(Long userId, Long guestBookId, SaveGuestBookRequest request) {
        GuestBook guestBook = guestBookRepository.findById(guestBookId)
                .orElseThrow(GuestBookNotFound::new);

        if (!guestBook.getUser().getId().equals(userId)) {
            throw new InvalidRequest();
        }

        guestBook.updateGuestBook(request.getContent());
    }

    //내가 쓴 방명록 삭제
    @Transactional
    public void deleteMyWrittenGuestBook(Long userId, Long guestBookId) {
        GuestBook guestBook = guestBookRepository.findById(guestBookId)
                .orElseThrow(GuestBookNotFound::new);

        if (!guestBook.getUser().getId().equals(userId)) {
            throw new InvalidRequest();
        }

        guestBookRepository.deleteById(guestBookId);
    }

    //자신의 명함 방명록 삭제
    @Transactional
    public void deleteMyBusinessCardGuestBook(Long businessCardId, Long guestBookId) {
        GuestBook guestBook = guestBookRepository.findByBusinessCardIdAndId(businessCardId, guestBookId)
                .orElseThrow();
        guestBookRepository.delete(guestBook);
    }

    private void notifyGuestBookInfo(BusinessCard businessCard, User writer) {
        NotificationRequest event = NotificationRequest.builder()
                .user(businessCard.getUser())
                .content(writer.getNickName() + EventContent.SAVE_GUESTBOOK_CONTENT.getEventContent())
                .url(EventUrl.SAVE_GUESTBOOK_URL_FRONT.getEventUrl() + businessCard.getId().toString() + EventUrl.SAVE_GUESTBOOK_URL_BACK.getEventUrl())
                .type(NotificationType.NEW_GUESTBOOK)
                .build();
        eventPublisher.publishEvent(event);
    }

    //자신의 명함의 방명록
    public List<GuestBookResponse> getMyGuestBooks(Long businessCardId, Pageable pageable) {
        List<GuestBook> guestBooks = guestBookRepository.findGuestBooksByBusinessCardId(businessCardId, pageable);
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

    //방명록 좋아요
    @Transactional
    public GuestBookLikeResponse guestBookLike(Long businessCardId, Long guestBookId) {
        GuestBook guestBook = guestBookRepository.findByBusinessCardIdAndId(businessCardId, guestBookId)
                .orElseThrow(GuestBookNotFound::new);

        guestBook.like();

        return new GuestBookLikeResponse(guestBook.getGuestBookLike());
    }

    //방명록 좋아요 취소
    @Transactional
    public GuestBookLikeResponse cancelGuestBookLike(Long businessCardId, Long guestBookId) {
        GuestBook guestBook = guestBookRepository.findByBusinessCardIdAndId(businessCardId, guestBookId)
                .orElseThrow(GuestBookNotFound::new);

        guestBook.cancelLike();

        return new GuestBookLikeResponse(guestBook.getGuestBookLike());
    }

}
