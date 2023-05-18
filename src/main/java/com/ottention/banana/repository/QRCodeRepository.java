package com.ottention.banana.repository;

import com.ottention.banana.entity.QRCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QRCodeRepository extends JpaRepository<QRCode, Long> {
}
