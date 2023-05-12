package com.ottention.banana.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class QRCode {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "qrcode_id")
    private Long id;

    private String businessCardAddress; //명함 주소 ex)/businessCard/1 QR코드를 통해 명함으로 접근 가능

    @Lob
    private byte[] qrCodeImage; //QR코드 값

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @Builder
    public QRCode(String businessCardAddress, byte[] qrCodeImage, BusinessCard businessCard) {
        this.businessCardAddress = businessCardAddress;
        this.qrCodeImage = qrCodeImage;
        businessCard.createQrCode(this);
    }

}
