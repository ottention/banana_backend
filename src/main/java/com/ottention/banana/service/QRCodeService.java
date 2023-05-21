package com.ottention.banana.service;

import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.ottention.banana.entity.BusinessCard;
import com.ottention.banana.entity.QRCode;
import com.ottention.banana.exception.BusinessCardNotFound;
import com.ottention.banana.exception.QRCodeGenerationException;
import com.ottention.banana.exception.QRCodeNotFound;
import com.ottention.banana.repository.BusinessCardRepository;
import com.ottention.banana.repository.QRCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.google.zxing.BarcodeFormat.QR_CODE;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QRCodeService {

    private final QRCodeRepository qrCodeRepository;
    private final BusinessCardRepository businessCardRepository;

    @Transactional
    public void generateAndSaveQrCode(String text, Long businessCardId) {
        BusinessCard businessCard = getBusinessCardById(businessCardId);
        byte[] qrCodeImage = generateQrCodeImage(text);
        saveQrCode(qrCodeImage, text, businessCard);
    }

    private BusinessCard getBusinessCardById(Long businessCardId) {
        return businessCardRepository.findById(businessCardId)
                .orElseThrow(BusinessCardNotFound::new);
    }

    private byte[] generateQrCodeImage(String text) {
        try {
            BitMatrix bitMatrix = new QRCodeWriter().encode(text, QR_CODE, 262, 262);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            return outputStream.toByteArray();
        } catch (WriterException | IOException e) {
            throw new QRCodeGenerationException();
        }
    }

    private void saveQrCode(byte[] qrCodeImage, String address, BusinessCard businessCard) {
        QRCode qrCode = QRCode.builder()
                .businessCardAddress(address)
                .qrCodeImage(qrCodeImage)
                .businessCard(businessCard)
                .build();
        qrCodeRepository.save(qrCode);
    }

    public byte[] getQrCodeImageById(Long businessCardId) {
        BusinessCard businessCard = businessCardRepository.findById(businessCardId)
                .orElseThrow();

        QRCode qrCode = businessCard.getQrCode();

        return qrCode.getQrCodeImage();
    }
}