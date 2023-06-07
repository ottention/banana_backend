package com.ottention.banana.controller;

import com.ottention.banana.config.Login;
import com.ottention.banana.dto.request.LoginUser;
import com.ottention.banana.dto.request.SaveBackBusinessCardRequest;
import com.ottention.banana.dto.request.SaveFrontBusinessCardRequest;
import com.ottention.banana.dto.response.businesscard.BusinessCardResponse;
import com.ottention.banana.service.BusinessCardService;
import com.ottention.banana.service.QRCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.ottention.banana.AddressConstant.ADDRESS;
import static org.springframework.http.MediaType.*;

@RestController
@RequiredArgsConstructor
public class BusinessCardController {

    private final BusinessCardService businessCardService;
    private final QRCodeService qrCodeService;

    @PostMapping(value = "/save", consumes = {APPLICATION_JSON_VALUE, MULTIPART_FORM_DATA_VALUE})
    public Long saveBusinessCard(@Login LoginUser user, @RequestPart(required = false) SaveFrontBusinessCardRequest frontRequest,
                                 @RequestPart(required = false) SaveBackBusinessCardRequest backRequest,
                                 @RequestPart(required = false) List<MultipartFile> frontImages,
                                 @RequestPart(required = false) List<MultipartFile> backImages) {
        Long businessCardId = businessCardService.save(user.getId(), frontRequest, backRequest, frontImages, backImages);
        qrCodeService.generateAndSaveQrCode(ADDRESS + businessCardId, businessCardId);
        return businessCardId;
    }

    @GetMapping("/businessCard/front/{businessCardId}")
    public BusinessCardResponse getFrontBusinessCard(@PathVariable Long businessCardId) {
        return businessCardService.getFrontBusinessCard(businessCardId);
    }

    @GetMapping("/businessCard/back/{businessCardId}")
    public BusinessCardResponse getBackBusinessCard(@PathVariable Long businessCardId) {
        return businessCardService.getBackBusinessCard(businessCardId);
    }

    @GetMapping(value = "/businessCard/{businessCardId}/qrcode", produces = IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getQRCode(@PathVariable Long businessCardId) {
        byte[] qrCodeImage = qrCodeService.getQrCodeImageById(businessCardId);
        return ResponseEntity.ok(qrCodeImage);
    }

}
