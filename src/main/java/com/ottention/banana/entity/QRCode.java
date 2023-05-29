package com.ottention.banana.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class QRCode extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "qrcode_id")
    private Long id;

    private String businessCardAddress; //명함 주소 ex)/businessCard/1 QR코드를 통해 명함으로 접근 가능

    @Lob
    private byte[] qrCodeImage; //QR코드 값

    @Builder
    public QRCode(String businessCardAddress, byte[] qrCodeImage, BusinessCard businessCard) {
        this.businessCardAddress = businessCardAddress;
        this.qrCodeImage = qrCodeImage;
        businessCard.createQrCode(this);
    }

}
