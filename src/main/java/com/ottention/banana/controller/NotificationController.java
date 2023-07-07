package com.ottention.banana.controller;

import com.ottention.banana.config.Login;
import com.ottention.banana.dto.request.LoginUser;
import com.ottention.banana.entity.User;
import com.ottention.banana.service.notification.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("/banana")
public class NotificationController {
    NotificationService notificationService;

    @GetMapping(value = "/subscribe", produces = "text/event-stream")
    public SseEmitter notification(@Login LoginUser user, @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        return notificationService.subscribe(user.getId(), lastEventId);
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
