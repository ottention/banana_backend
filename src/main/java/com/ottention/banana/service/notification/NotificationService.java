package com.ottention.banana.service.notification;

import com.ottention.banana.dto.response.NotificationResponse;
import com.ottention.banana.entity.User;
import com.ottention.banana.entity.notification.Notification;
import com.ottention.banana.entity.notification.NotificationType;
import com.ottention.banana.mapper.SseMapper;
import com.ottention.banana.repository.notification.EmitterRepository;
import com.ottention.banana.repository.notification.EmitterRepositoryImpl;
import com.ottention.banana.repository.notification.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final EmitterRepository emitterRepository = new EmitterRepositoryImpl();
    private final SseMapper sseMapper;

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60 * 6;  //SseEmitter 연결 6시간 지속

    private String makeTimeIncludedId(Long userId) {
        return userId + "_" + System.currentTimeMillis();
    }

    private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .data(data));
        } catch (IOException e) {
            emitterRepository.deleteById(emitterId);
        }
    }

    public SseEmitter subscribe(Long userId, String lastEventId) {
        String emitterId = makeTimeIncludedId(userId);
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));
        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        //503 에러 방지용 더미 이벤트 전송
        String eventId = makeTimeIncludedId(userId);
        sendNotification(emitter, eventId, emitterId, "EventStream Created. [userId=" + userId + "]");

        //클라이언트가 미수신한 Event 목록이 존재하면 전송하여 이벤트 유실 예방
        if (hasLostData(lastEventId)) {
            sendLostData(lastEventId, userId, emitterId, emitter);
        }
        log.info("subscribe notification");
        return emitter;
    }

    private boolean hasLostData(String lastEventId) {
        return !lastEventId.isEmpty();
    }

    private void sendLostData(String lastEventId, Long userId, String emitterId, SseEmitter emitter) {
        Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithByUserId(String.valueOf(userId));
        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
    }

    public void send(User user, String content, String url, NotificationType notificationType) {
        Notification notification = notificationRepository.save(creatNotification(user, notificationType, content, url));
        String userId = String.valueOf(user.getId());
        String eventId = userId + "_" + System.currentTimeMillis();
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByUserId(userId);
        emitters.forEach(
                (key, emitter) -> {
                    emitterRepository.saveEventCache(key, notification);
                    sendNotification(emitter, eventId, key, sseMapper.toResponse(notification));
                }
        );
    }

    private Notification creatNotification(User user, NotificationType notificationType, String content, String url) {
        return Notification.builder()
                .user(user)
                .notificationType(notificationType)
                .content(content)
                .url(url)
                .isRead(false)
                .build();
    }

    public List<NotificationResponse> getAll(Long id, Pageable pageable) {
        List<Notification> notifications = notificationRepository.findAllByUserId(id, pageable);
        return notifications.stream().map(sseMapper::toResponse).toList();
    }

    public void deleteAll(Long userId) {
        notificationRepository.deleteAllByUserId(userId);
    }

    public void deleteById(Long id) {
        notificationRepository.deleteById(id);
    }
}

