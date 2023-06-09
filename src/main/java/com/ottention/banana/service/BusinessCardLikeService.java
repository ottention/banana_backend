package com.ottention.banana.service;

import com.ottention.banana.entity.BusinessCard;
import com.ottention.banana.entity.BusinessCardLike;
import com.ottention.banana.entity.User;
import com.ottention.banana.exception.BusinessCardNotFound;
import com.ottention.banana.exception.UserNotFound;
import com.ottention.banana.exception.ZeroLikesError;
import com.ottention.banana.repository.BusinessCardLikeRepository;
import com.ottention.banana.repository.BusinessCardRepository;
import com.ottention.banana.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BusinessCardLikeService {

    private final BusinessCardLikeRepository businessCardLikeRepository;
    private final BusinessCardRepository businessCardRepository;
    private final UserRepository userRepository;

    public int like(Long userId, Long businessCardId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFound::new);

        BusinessCard businessCard = businessCardRepository.findById(businessCardId)
                .orElseThrow(BusinessCardNotFound::new);

        Optional<BusinessCardLike> businessCardLike = businessCardLikeRepository.findByUserIdAndBusinessCardId(userId, businessCardId);

        BusinessCardLike like = BusinessCardLike.builder()
                .user(user)
                .businessCard(businessCard)
                .like(businessCardLike)
                .build();

        businessCardLikeRepository.save(like);

        return businessCard.getLikeCount();
    }

    public int cancelLike(Long userId, Long businessCardId) {
        BusinessCardLike businessCardLike = businessCardLikeRepository.findByUserIdAndBusinessCardId(userId, businessCardId)
                .orElseThrow(ZeroLikesError::new);

        businessCardLikeRepository.delete(businessCardLike);

        return businessCardLike.cancelLike();
    }

}
