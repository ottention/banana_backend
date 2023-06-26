package com.ottention.banana.controller;

import com.ottention.banana.config.Login;
import com.ottention.banana.dto.request.LoginUser;
import com.ottention.banana.dto.request.SaveBackBusinessCardRequest;
import com.ottention.banana.dto.request.SaveFrontBusinessCardRequest;
import com.ottention.banana.dto.request.SaveTagRequest;
import com.ottention.banana.dto.response.businesscard.BusinessCardResponse;
import com.ottention.banana.service.BusinessCardService;
import com.ottention.banana.service.QRCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.ottention.banana.AddressConstant.ADDRESS;
import static org.springframework.http.MediaType.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BusinessCardController {

    private final BusinessCardService businessCardService;
    private final QRCodeService qrCodeService;

    @PostMapping(value = "/businessCard/save", consumes = {APPLICATION_JSON_VALUE, MULTIPART_FORM_DATA_VALUE})
    public Long saveBusinessCard(@Login LoginUser user, @RequestPart(required = false) SaveFrontBusinessCardRequest frontRequest,
                                 @RequestPart(required = false) SaveBackBusinessCardRequest backRequest,
                                 @RequestPart(required = false) List<MultipartFile> frontImages,
                                 @RequestPart(required = false) List<MultipartFile> backImages,
                                 @RequestPart(required = false) SaveTagRequest tagRequest) {
        Long businessCardId = businessCardService.save(user.getId(), frontRequest, backRequest, frontImages, backImages, tagRequest);
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

    @GetMapping(value = "/businessCard/{businessCardId}/qrcode/v1", produces = IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getQRCodeV1(@PathVariable Long businessCardId) {
        byte[] qrCodeImage = qrCodeService.getQrCodeImageById(businessCardId);
        return ResponseEntity.ok(qrCodeImage);
    }

    @GetMapping("/businessCard/{businessCardId}/qrcode/v2")
    public byte[] getQRCodeV2(@PathVariable Long businessCardId) {
        return qrCodeService.getQrCodeImageById(businessCardId);
    }

    @GetMapping("/businessCard/{businessCardId}/qrcode/v3")
    public String getQRCodeV3(@PathVariable Long businessCardId) {
        return qrCodeService.getQrCodeImageById(businessCardId).toString();
    }

}
