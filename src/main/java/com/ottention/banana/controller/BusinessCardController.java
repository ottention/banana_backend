package com.ottention.banana.controller;

import com.ottention.banana.config.Login;
import com.ottention.banana.request.LoginUser;
import com.ottention.banana.request.SaveBusinessCardRequest;
import com.ottention.banana.response.BusinessCardResponse;
import com.ottention.banana.service.BusinessCardService;
import com.ottention.banana.service.QRCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.ottention.banana.Const.ADDRESS;
import static org.springframework.http.MediaType.*;

@RestController("/businessCard")
@RequiredArgsConstructor
public class BusinessCardController {

    private final BusinessCardService businessCardService;
    private final QRCodeService qrCodeService;

    @PostMapping(value = "/save", consumes = {APPLICATION_JSON_VALUE, MULTIPART_FORM_DATA_VALUE})
    public void saveBusinessCard(@Login LoginUser user, @RequestPart SaveBusinessCardRequest request,
                                 @RequestPart List<MultipartFile> files) {
        Long businessCardId = businessCardService.save(user, request, files);
        qrCodeService.generateAndSaveQrCode(ADDRESS + businessCardId, businessCardId);
    }

    @GetMapping("/businessCard/{businessCardId}")
    public BusinessCardResponse getBusinessCard(@PathVariable Long businessCardId) {
        return businessCardService.getBusinessCard(businessCardId);
    }

    @GetMapping(value = "/businessCard/{businessCardId}/qrcode", produces = IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getQRCode(@PathVariable Long businessCardId) {
        byte[] qrCodeImage = qrCodeService.getQrCodeImageById(businessCardId);
        return ResponseEntity.ok(qrCodeImage);
    }

}
