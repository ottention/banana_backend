package com.ottention.banana.service;

import com.ottention.banana.dto.request.notification.NotificationRequest;
import com.ottention.banana.dto.response.businesscard.BusinessCardLikeResponse;
import com.ottention.banana.entity.BusinessCard;
import com.ottention.banana.entity.BusinessCardLike;
import com.ottention.banana.entity.User;
import com.ottention.banana.entity.notification.NotificationType;
import com.ottention.banana.exception.BusinessCardNotFound;
import com.ottention.banana.exception.DuplicationLikeException;
import com.ottention.banana.exception.UserNotFound;
import com.ottention.banana.exception.ZeroLikesError;
import com.ottention.banana.repository.BusinessCardLikeRepository;
import com.ottention.banana.repository.BusinessCardRepository;
import com.ottention.banana.repository.UserRepository;
import com.ottention.banana.service.event.EventContent;
import com.ottention.banana.service.event.EventUrl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BusinessCardLikeService {

    private final BusinessCardLikeRepository businessCardLikeRepository;
    private final BusinessCardRepository businessCardRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    public BusinessCardLikeResponse like(Long userId, Long businessCardId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFound::new);

        BusinessCard businessCard = businessCardRepository.findById(businessCardId)
                .orElseThrow(BusinessCardNotFound::new);

        //사용자는 하나의 명함에 좋아요 한 번만 누를 수 있음
        if (businessCardLikeRepository.existsByUserIdAndBusinessCardId(userId, businessCardId)) {
            throw new DuplicationLikeException();
        }

        BusinessCardLike like = BusinessCardLike.builder()
                .user(user)
                .businessCard(businessCard)
                .build();

        businessCardLikeRepository.save(like);

        //명함 좋아요 알림 전송
        notifyBusinessCardLikeInfo(like);
        return new BusinessCardLikeResponse(businessCard.getLikeCount(), true);
    }

    private void notifyBusinessCardLikeInfo(BusinessCardLike like) {
        NotificationRequest event = NotificationRequest.builder()
                .user(like.getUser())
                .content(EventContent.BUSINESS_CARD_LIKE_CONTENT.getEventContent())
                .url(EventUrl.BUSINESS_CARD_LIKE_URL.getEventUrl() + like.getBusinessCard().getId().toString())
                .type(NotificationType.BUSINESS_CARD_LIKE)
                .build();
        eventPublisher.publishEvent(event);
    }

    public BusinessCardLikeResponse cancelLike(Long userId, Long businessCardId) {
        BusinessCardLike businessCardLike = businessCardLikeRepository.findByUserIdAndBusinessCardId(userId, businessCardId)
                .orElseThrow(ZeroLikesError::new);

        businessCardLikeRepository.delete(businessCardLike);

        return new BusinessCardLikeResponse(businessCardLike.cancelLike(), false);
    }

}
