package com.ottention.banana.service;

import com.ottention.banana.entity.BusinessCard;
import com.ottention.banana.entity.QRCode;
import com.ottention.banana.entity.User;
import com.ottention.banana.exception.qrcode.QRCodeNotFound;
import com.ottention.banana.repository.BusinessCardRepository;
import com.ottention.banana.repository.QRCodeRepository;
import com.ottention.banana.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@SpringBootTest
class QRCodeServiceTest {

    @Autowired
    private QRCodeService qrCodeService;

    @Autowired
    private QRCodeRepository qrCodeRepository;

    @Autowired
    private BusinessCardRepository businessCardRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void clear() {
        businessCardRepository.deleteAll();
        qrCodeRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("QR코드 생성 테스트")
    void generateQRCodeTest() {
        //given
        User user = User.builder()
                .nickName("nickname")
                .email("email")
                .build();

        userRepository.save(user);

        BusinessCard businessCard = BusinessCard.builder()
                .isRepresent(true)
                .isPublic(true)
                .user(user)
                .build();

        businessCardRepository.save(businessCard);

        Long qrCodeId = qrCodeService.generateAndSaveQrCode("localhost:8080/qrcode", businessCard.getId());

        //when
        QRCode qrCode = qrCodeRepository.findById(qrCodeId)
                .orElseThrow(QRCodeNotFound::new);

        //then
        assertThat(qrCode.getBusinessCardAddress()).isEqualTo("localhost:8080/qrcode");
        assertThat(qrCode.getId()).isEqualTo(qrCodeId);
        assertNotNull(qrCode.getQrCodeImage());
    }


}