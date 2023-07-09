package com.ottention.banana.controller;

import com.ottention.banana.config.Login;
import com.ottention.banana.dto.request.LoginUser;
import com.ottention.banana.dto.response.NotificationResponse;
import com.ottention.banana.dto.response.businesscard.CategoryCardsResponse;
import com.ottention.banana.entity.User;
import com.ottention.banana.service.notification.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class NotificationController {
    NotificationService notificationService;

    @GetMapping(value = "/subscribe", produces = "text/event-stream")
    public SseEmitter notification(@Login LoginUser user, @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        return notificationService.subscribe(user.getId(), lastEventId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/notifications")
    public List<NotificationResponse> get(@Login LoginUser user, @PageableDefault(sort = "modifiedDate", direction = DESC) Pageable pageable) {
        return notificationService.getAll(user.getId(), pageable);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/notifications")
    public void deleteAll(@Login LoginUser user) {

        notificationService.deleteAll(user.getId());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/notifications/{id}")
    public void delete(@PathVariable Long id) {
        notificationService.deleteById(id);
    }
}
