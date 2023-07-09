package com.ottention.banana.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class QRCodeAddressResponse {
    private final String address;
}
