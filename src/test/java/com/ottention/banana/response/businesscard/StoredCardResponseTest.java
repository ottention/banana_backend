package com.ottention.banana.response.businesscard;

import com.ottention.banana.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StoredCardResponseTest {
    //qrcode
    final String businessCardAddress = "businessCard/1";
    final byte[] qrCodeImage = businessCardAddress.getBytes();

    //card
    final Boolean isPublic = true;

    //content
    final String content = "User";
    final int xAxis = 20;
    final int yAxis = 80;

    //user
    final String nickname = "Limyeon";
    final String email = "yeonjy@gmail.com";

    //card
    final String name = "[잇타] User";
    final Boolean isBookmarked = true;


    @BeforeEach
    void setData() {
        final User user1 = new User(nickname, email);
        final BusinessCard businessCard1 = new BusinessCard(isPublic, user1);
        final QRCode qrCode1 = new QRCode(businessCardAddress, qrCodeImage, businessCard1);
        final BusinessCardContent content1 = new BusinessCardContent(content, xAxis, yAxis);
        content1.addBusinessCard(businessCard1);
    }

    @Test
    @DisplayName("저장된 명함 내용 응답 DTO 테스트")
    void testStoredCardContent() {
        //given
        User user1 = new User(nickname, email);
        BusinessCard businessCard1 = new BusinessCard(isPublic, user1);
        QRCode qrCode1 = new QRCode(businessCardAddress, qrCodeImage, businessCard1);
        BusinessCardContent content1 = new BusinessCardContent(content, xAxis, yAxis);
        content1.addBusinessCard(businessCard1);

        //when
        StoredCardContentResponse cardContentResponse1 = new StoredCardContentResponse(content1);

        //then
        assertThat(cardContentResponse1.getCid()).isEqualTo(content1.getId());
        assertThat(cardContentResponse1.getContent()).isEqualTo(content);
        assertThat(cardContentResponse1.getXAxis()).isEqualTo(xAxis);
        assertThat(cardContentResponse1.getYAxis()).isEqualTo(yAxis);
        assertThat(cardContentResponse1.getBid()).isEqualTo(content1.getBusinessCard().getId());
    }

    @Test
    @DisplayName("저장된 명함 응답 DTO 테스트")
    void testStoredCard() {
        //given
        String categoryName = "[대외활동] 잇타 멤버들 명함";
        User user1 = new User(nickname, email);
        BusinessCard businessCard1 = new BusinessCard(isPublic, user1);
        QRCode qrCode1 = new QRCode(businessCardAddress, qrCodeImage, businessCard1);
        BusinessCardContent content1 = new BusinessCardContent(content, xAxis, yAxis);
        Category category = new Category(categoryName, user1);
        StoredBusinessCard storedCard = new StoredBusinessCard(name, isBookmarked, businessCard1, category, user1);

        //when
        StoredCardResponse storedCardResponse1 = new StoredCardResponse(storedCard);

        //then
        assertThat(storedCardResponse1.getName()).isEqualTo(name);
        assertThat(storedCardResponse1.getIsBookmarked()).isEqualTo(isBookmarked);
        assertThat(storedCardResponse1.getCid()).isEqualTo(businessCard1.getId());

    }
}