package com.ottention.banana.service;

import com.ottention.banana.dto.request.SaveBusinessCardRequest;
import com.ottention.banana.dto.response.businesscard.BusinessCardIdResponse;
import com.ottention.banana.dto.response.businesscard.BusinessCardResponse;
import com.ottention.banana.entity.*;
import com.ottention.banana.exception.*;
import com.ottention.banana.repository.BusinessCardLikeRepository;
import com.ottention.banana.repository.BusinessCardRepository;
import com.ottention.banana.repository.GuestBookRepository;
import com.ottention.banana.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ottention.banana.AppConstant.INITIAL_BUSINESS_CARD_COUNT;
import static com.ottention.banana.AppConstant.MAX_BUSINESS_CARD_COUNT;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BusinessCardService {

    private final BusinessCardContentService businessCardContentService;
    private final BusinessCardRepository businessCardRepository;
    private final UserRepository userRepository;
    private final GuestBookRepository guestBookRepository;
    private final BusinessCardLikeRepository businessCardLikeRepository;
    private final ImageService imageService;
    private final LinkService linkService;
    private final TagService tagService;

    @Transactional
    public Long saveBusinessCard(Long userId, SaveBusinessCardRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFound::new);

        List<BusinessCard> businessCards = businessCardRepository.findByUserId(userId);
        if (businessCards.size() >= MAX_BUSINESS_CARD_COUNT) {
            throw new BusinessCardLimitExceededException();
        }

        log.info("isPresent = {}", request.getIsPresent());
        log.info("isPublic = {}", request.getIsPublic());
        log.info("frontTemplateColor = {}", request.getFrontTemplateColor());
        log.info("backTemplateColor = {}", request.getBackTemplateColor());

        //처음 명함은 무조건 대표 명함
        if (businessCards.size() == INITIAL_BUSINESS_CARD_COUNT) {
            BusinessCard businessCard = createInitialBusinessCard(request, user);
            return businessCardRepository.save(businessCard).getId();
        }

        updateRepresentativeStatus(request, businessCards);

        BusinessCard businessCard = createSubsequentBusinessCard(request, user);
        return businessCardRepository.save(businessCard).getId();
    }

    public List<BusinessCardIdResponse> getBusinessCardIds(Long userId) {
        List<BusinessCard> businessCards = businessCardRepository.findByUserId(userId);
        return businessCards.stream().map(b -> new BusinessCardIdResponse(b.getId()))
                .collect(toList());
    }

    @Transactional
    public void updateBusinessCard(Long userId, Long businessCardId, SaveBusinessCardRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFound::new);

        BusinessCard businessCard = businessCardRepository.findById(businessCardId)
                .orElseThrow(BusinessCardNotFound::new);

        validateSameUser(user, businessCard);

        deleteBusinessCardContents(businessCardId);

        businessCardContentService.saveBusinessCardContents(request, businessCard);
        imageService.saveImage(request, businessCard);
        linkService.saveLink(request, businessCard);
        tagService.saveTag(request.getTags(), businessCard);
    }

    private void validateSameUser(User user, BusinessCard businessCard) {
        if (!user.getId().equals(businessCard.getUser().getId())) {
            throw new InvalidRequest();
        }
    }

    private void updateRepresentativeStatus(SaveBusinessCardRequest request, List<BusinessCard> businessCards) {
        if (request.getIsPresent()) {
            for (BusinessCard businessCard : businessCards) {
                businessCard.updateRepresent(false);
            }
        }
    }

    private BusinessCard createInitialBusinessCard(SaveBusinessCardRequest request, User user) {
        return createBusinessCard(request, user, true);
    }

    private BusinessCard createSubsequentBusinessCard(SaveBusinessCardRequest request, User user) {
        return createBusinessCard(request, user, request.getIsPresent());
    }

    private BusinessCard createBusinessCard(SaveBusinessCardRequest request, User user, boolean isRepresent) {
        BusinessCard businessCard = BusinessCard.builder()
                .isPublic(request.getIsPublic())
                .isRepresent(isRepresent)
                .frontTemplateColor(request.getFrontTemplateColor())
                .backTemplateColor(request.getBackTemplateColor())
                .user(user)
                .build();

        businessCardContentService.saveBusinessCardContents(request, businessCard);
        imageService.saveImage(request, businessCard);
        linkService.saveLink(request, businessCard);
        tagService.saveTag(request.getTags(), businessCard);
        return businessCard;
    }

    public BusinessCardResponse getBusinessCard(Long userId, Long businessCardId) {
        BusinessCard businessCard = businessCardRepository.findById(businessCardId)
                .orElseThrow(BusinessCardNotFound::new);

        if (!businessCard.getUser().getId().equals(userId)) {
            if (!businessCard.getIsPublic()) {
                throw new PrivateBusinessCardException();
            }
            return getBusinessCardResponse(businessCardId, businessCard);
        }

        return getBusinessCardResponse(businessCardId, businessCard);
    }

    private BusinessCardResponse getBusinessCardResponse(Long businessCardId, BusinessCard businessCard) {
        List<BusinessCardContent> frontContents = businessCardContentService.getFrontContents(businessCardId);
        List<BusinessCardContent> backContents = businessCardContentService.getBackContents(businessCardId);

        List<Image> frontImages = imageService.getFrontImages(businessCardId);
        List<Image> backImages = imageService.getBackImages(businessCardId);

        List<Link> frontLinks = linkService.getFrontLinks(businessCardId);
        List<Link> backLinks = linkService.getBackLinks(businessCardId);

        List<Tag> tags = tagService.getTags(businessCardId);

        return new BusinessCardResponse(businessCard, frontContents, frontLinks, frontImages,
                backContents, backLinks, backImages, tags);
    }

    @Transactional
    public void deleteBusinessCard(Long businessCardId) {
        BusinessCard businessCard = businessCardRepository.findById(businessCardId)
                .orElseThrow(BusinessCardNotFound::new);

        List<GuestBook> guestBooks = guestBookRepository.findAllByBusinessCardId(businessCardId);
        guestBookRepository.deleteAllInBatch(guestBooks);

        List<BusinessCardLike> businessCardLikes = businessCardLikeRepository.findAllByBusinessCardId(businessCardId);
        businessCardLikeRepository.deleteAllInBatch(businessCardLikes);

        deleteBusinessCardContents(businessCardId);

        businessCardRepository.delete(businessCard);
    }

    private void deleteBusinessCardContents(Long businessCardId) {
        List<BusinessCardContent> frontContents = businessCardContentService.getFrontContents(businessCardId);
        List<BusinessCardContent> backContents = businessCardContentService.getBackContents(businessCardId);
        businessCardContentService.deleteBusinessCardContents(frontContents, backContents);

        List<Image> frontImages = imageService.getFrontImages(businessCardId);
        List<Image> backImages = imageService.getBackImages(businessCardId);
        imageService.deleteImages(frontImages, backImages);

        List<Link> frontLinks = linkService.getFrontLinks(businessCardId);
        List<Link> backLinks = linkService.getBackLinks(businessCardId);
        linkService.deleteLinks(frontLinks, backLinks);

        List<Tag> tags = tagService.getTags(businessCardId);
        tagService.deleteTags(tags);
    }

}
