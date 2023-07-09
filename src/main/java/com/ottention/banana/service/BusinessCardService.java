package com.ottention.banana.service;

import com.ottention.banana.dto.request.SaveBusinessCardRequest;
import com.ottention.banana.dto.response.businesscard.BusinessCardResponse;
import com.ottention.banana.dto.response.businesscard.BusinessCardSettingStatus;
import com.ottention.banana.entity.*;
import com.ottention.banana.exception.*;
import com.ottention.banana.repository.BusinessCardRepository;
import com.ottention.banana.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ottention.banana.AppConstant.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BusinessCardService {

    private final BusinessCardContentService businessCardContentService;
    private final BusinessCardRepository businessCardRepository;
    private final UserRepository userRepository;
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

    @Transactional
    public void updateBusinessCard(Long userId, Long businessCardId, SaveBusinessCardRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFound::new);

        BusinessCard businessCard = businessCardRepository.findById(businessCardId)
                .orElseThrow(BusinessCardNotFound::new);

        validateSameUser(user, businessCard);

        List<BusinessCardContent> frontContents = businessCardContentService.getFrontContents(businessCardId);
        List<BusinessCardContent> backContents = businessCardContentService.getBackContents(businessCardId);
        businessCardContentService.deleteBusinessCardContents(frontContents);
        businessCardContentService.deleteBusinessCardContents(backContents);

        List<Image> frontImages = imageService.getFrontImages(businessCardId);
        List<Image> backImages = imageService.getBackImages(businessCardId);
        imageService.deleteImages(frontImages);
        imageService.deleteImages(backImages);

        List<Link> frontLinks = linkService.getFrontLinks(businessCardId);
        List<Link> backLinks = linkService.getBackLinks(businessCardId);
        linkService.deleteLinks(frontLinks);
        linkService.deleteLinks(backLinks);

        List<Tag> tags = tagService.getTags(businessCardId);
        tagService.deleteTags(tags);

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

    //알림창 메시지 반환 메서드
    public BusinessCardSettingStatus getSettingStatusMessage(Long userId) {
        List<BusinessCard> businessCards = businessCardRepository.findByUserId(userId);
        for (int businessCardNumber = 0; businessCardNumber < businessCards.size(); businessCardNumber++) {
            if (businessCards.get(businessCardNumber).getIsRepresent()) {
                String message = generateMessage(businessCardNumber + 1);
                return new BusinessCardSettingStatus(message);
            }
        }
        return new BusinessCardSettingStatus("");
    }

    private String generateMessage(int businessCardNumber) {
        return String.format(MAIN_CARD_REGISTERED_MESSAGE, businessCardNumber);
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

}
