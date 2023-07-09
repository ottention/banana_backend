package com.ottention.banana.entity.notification;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class NotificationContentTest {

    private Validator validator = null;

    @BeforeEach
    public void setupValidator() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    public void 알림_내용_공백시_실패() throws Exception {
        //given
        NotificationContent content = new NotificationContent(" ");
        //when
        Set<ConstraintViolation<NotificationContent>> violations = validator.validate(content);
        //then
        assertThat(violations.size()).isEqualTo(1);
    }
}