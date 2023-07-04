package com.ottention.banana.service;

import com.ottention.banana.dto.LinkDto;
import com.ottention.banana.dto.request.SaveBusinessCardRequest;
import com.ottention.banana.entity.BusinessCard;
import com.ottention.banana.entity.Link;
import com.ottention.banana.repository.LinkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LinkService {

    private final LinkRepository linkRepository;

    @Transactional
    public void saveLink(SaveBusinessCardRequest request, BusinessCard businessCard) {
        saveFrontLink(request.getFrontLinks(), businessCard);
        saveBackLink(request.getBackLinks(), businessCard);
    }

    private void saveFrontLink(List<LinkDto> frontLinks, BusinessCard businessCard) {
        for (LinkDto frontLink : frontLinks) {
            Link link = Link.builder()
                    .isFront(frontLink.getIsFront())
                    .link(frontLink.getLink())
                    .linkText(frontLink.getLinkText())
                    .businessCard(businessCard)
                    .build();

            log.info("frontLink.getIsFront()", frontLink.getIsFront());
            log.info("frontLink.getLinkText()", frontLink.getLinkText());
            log.info("frontLink.getLink()", frontLink.getLink());

            linkRepository.save(link);
        }
    }

    private void saveBackLink(List<LinkDto> backLinks, BusinessCard businessCard) {
        for (LinkDto backLink : backLinks) {
            Link link = Link.builder()
                    .isFront(backLink.getIsFront())
                    .link(backLink.getLink())
                    .linkText(backLink.getLinkText())
                    .businessCard(businessCard)
                    .build();

            log.info("backLink.getIsFront()", backLink.getIsFront());
            log.info("backLink.getLinkText()", backLink.getLinkText());
            log.info("backLink.getLink()", backLink.getLink());

            linkRepository.save(link);
        }
    }

    public List<Link> getFrontLinks(Long businessCardId) {
        return linkRepository.findByBusinessCardIdAndIsFront(businessCardId, true);
    }

    public List<Link> getBackLinks(Long businessCardId) {
        return linkRepository.findByBusinessCardIdAndIsFront(businessCardId, false);
    }

}
