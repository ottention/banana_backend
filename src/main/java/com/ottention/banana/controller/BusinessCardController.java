package com.ottention.banana.controller;

import com.ottention.banana.config.Login;
import com.ottention.banana.dto.request.*;
import com.ottention.banana.dto.response.businesscard.BusinessCardIdResponse;
import com.ottention.banana.dto.response.businesscard.BusinessCardResponse;
import com.ottention.banana.dto.response.businesscard.BusinessCardSettingStatus;
import com.ottention.banana.service.BusinessCardService;
import com.ottention.banana.service.QRCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static com.ottention.banana.AppConstant.ADDRESS;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BusinessCardController {

    private final BusinessCardService businessCardService;
    private final QRCodeService qrCodeService;

    @PostMapping("/businessCard/save")
    public BusinessCardIdResponse saveBusinessCard(@Login LoginUser user, @RequestBody SaveBusinessCardRequest request) {
        Long businessCardId = businessCardService.saveBusinessCard(user.getId(), request);
        qrCodeService.generateAndSaveQrCode(ADDRESS + businessCardId, businessCardId);
        return new BusinessCardIdResponse(businessCardId);
    }

    @GetMapping("/businessCard/representative")
    public BusinessCardSettingStatus getSettingStatusMessage(@Login LoginUser user) {
        return businessCardService.getSettingStatusMessage(user.getId());
    }

    @GetMapping("/businessCard/{businessCardId}")
    public BusinessCardResponse getBusinessCard(@Login LoginUser user, @PathVariable Long businessCardId) {
        return businessCardService.getBusinessCard(user.getId(), businessCardId);
    }

    @GetMapping("/businessCard/{businessCardId}/qrcode")
    public String getQRCodeV3(@PathVariable Long businessCardId) {
        return qrCodeService.getQRCodeAddress(businessCardId);
    }

}
