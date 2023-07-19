package com.ottention.banana.service;

import com.ottention.banana.dto.response.businesscard.BusinessCardResponse;
import com.ottention.banana.entity.BusinessCard;
import com.ottention.banana.entity.BusinessCardLike;
import com.ottention.banana.entity.User;
import com.ottention.banana.repository.BusinessCardLikeRepository;
import com.ottention.banana.repository.BusinessCardRepository;
import com.ottention.banana.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class ChartServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BusinessCardRepository businessCardRepository;

    @Autowired
    private BusinessCardLikeRepository businessCardLikeRepository;

    @Autowired
    private TagService tagService;

    @Autowired
    private ChartService chartService;

    @Test
    @DisplayName("명함 탑 10 조회 테스트")
    void testGetTopTenBusinessCards() {
        //given
        User user = User.builder()
                .email("a")
                .nickName("a")
                .build();

        userRepository.save(user);

        //조회 되어야 할 데이터
        for (int i = 0; i < 10; i++) {
            BusinessCard businessCard = BusinessCard.builder()
                    .isPublic(true)
                    .isRepresent(true)
                    .backTemplateColor("#12345")
                    .frontTemplateColor("#12345")
                    .user(user)
                    .build();

            businessCardRepository.save(businessCard);

            Optional<BusinessCardLike> like = businessCardLikeRepository.findByUserIdAndBusinessCardId(user.getId(), businessCard.getId());

            BusinessCardLike businessCardLike = BusinessCardLike.builder()
                    .businessCard(businessCard)
                    .like(like)
                    .user(user)
                    .build();

            businessCardLikeRepository.save(businessCardLike);

            List<String> tags = List.of("잇타", "오텐션", "바나나");

            tagService.saveTag(tags, businessCard);
        }

        //조회 되면 안 되는 데이터
        for (int i = 0; i < 10; i++) {
            BusinessCard businessCard = BusinessCard.builder()
                    .isPublic(true)
                    .isRepresent(false)
                    .backTemplateColor("#54321")
                    .frontTemplateColor("#54321")
                    .user(user)
                    .build();

            businessCardRepository.save(businessCard);
        }

        //when
        List<BusinessCardResponse> topTenBusinessCards = chartService.getTopTenBusinessCards("잇타", user.getId());

        //then
        assertThat(topTenBusinessCards.size()).isEqualTo(10);
        for (BusinessCardResponse topTenBusinessCard : topTenBusinessCards) {
            assertThat(topTenBusinessCard.getFrontTemplateColor()).isEqualTo("#12345");
            assertThat(topTenBusinessCard.getBackTemplateColor()).isEqualTo("#12345");
            assertThat(topTenBusinessCard.getIsRepresent()).isTrue();
            assertThat(topTenBusinessCard.getIsPublic()).isTrue();
        }
    }

}