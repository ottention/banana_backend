package com.ottention.banana.repository.notification;

import com.ottention.banana.entity.User;
import com.ottention.banana.entity.notification.Notification;
import com.ottention.banana.entity.notification.NotificationType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

import static java.lang.Thread.sleep;

class EmitterRepositoryImplTest {

    private EmitterRepository emitterRepository = new EmitterRepositoryImpl();
    private Long DEFAULT_TIMEOUT = 60L * 1000L * 60L;

    @Test
    public void 새로운_Emitter_추가() {
        //given
        Long userId = 1L;
        String emitterId = userId + "_" + System.currentTimeMillis();
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);

        //when then
        Assertions.assertDoesNotThrow(() -> emitterRepository.save(emitterId, sseEmitter));
    }

    @Test
    public void 수신한_Event를_캐시에_저장() {
        //given
        Long userId = 1L;
        String eventCacheId = userId + "_" + System.currentTimeMillis();
        Notification notification = new Notification(User.builder().email("banana@naver.com").nickName("바나나").build(), NotificationType.GUESTBOOK_LIKE, "방명록에 좋아요를 눌렀습니다.", "url", false);

        //when then
        Assertions.assertDoesNotThrow(() -> emitterRepository.saveEventCache(eventCacheId, notification));
    }

    @Test
    void 특정_회원이_접속한_모든_Emitter_찾기() throws Exception{
        //given
        Long userId = 1L;
        for (int i = 0; i < 3; i++) {
            sleep(100);
            emitterRepository.save(userId + "_" + System.currentTimeMillis(), new SseEmitter(DEFAULT_TIMEOUT));
        }

        //when
        Map<String, SseEmitter> ActualResult = emitterRepository.findAllEmitterStartWithByUserId(String.valueOf(userId));

        //then
        Assertions.assertEquals(3, ActualResult.size());
    }

    @Test
    void 특정_회원에게_수신된_이벤트를_캐시에서_모두_찾기() throws Exception {
        //given
        Long userId = 1L;

        for (int i = 0; i < 3; i++) {
            emitterRepository.saveEventCache(userId + "_" + System.currentTimeMillis(), new Notification(User.builder().email("banana@naver.com").nickName("바나나").build(), NotificationType.GUESTBOOK_LIKE, "방명록에 좋아요를 눌렀습니다.", "url", false));
            sleep(100);
        }
        //when
        Map<String, Object> ActualResult = emitterRepository.findAllEventCacheStartWithByUserId(String.valueOf(userId));
        //then
        Assertions.assertEquals(3, ActualResult.size());
    }

    @Test
    void ID를_통해_Emitter를_Repository에서_제거() {
        //given
        Long userId = 1L;
        String emitterId = userId + "_" + System.currentTimeMillis();
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);

        //when
        emitterRepository.save(emitterId, sseEmitter);
        emitterRepository.deleteById(emitterId);

        //then
        Assertions.assertEquals(0, emitterRepository.findAllEmitterStartWithByUserId(emitterId).size());
    }

    @Test
    void 저장된_모든_Emitter_삭제() throws Exception {
        //given
        Long userId = 1L;
        for (int i = 0; i < 2; i++) {
            sleep(100);
            String emitterId = userId + "_" + System.currentTimeMillis();
            emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));
        }

        //when
        emitterRepository.deleteAllEmitterStartWithId(String.valueOf(userId));

        //then
        Assertions.assertEquals(0, emitterRepository.findAllEmitterStartWithByUserId(String.valueOf(userId)).size());
    }

    @Test
    void 수신한_이벤트를_캐시에_저장() throws Exception {
        Long userId = 1L;
        emitterRepository.saveEventCache(userId + "_" + System.currentTimeMillis(), new Notification(User.builder().email("banana@naver.com").nickName("바나나").build(), NotificationType.GUESTBOOK_LIKE, "방명록에 좋아요를 눌렀습니다.", "url", false));
        sleep(100);
        emitterRepository.saveEventCache(userId + "_" + System.currentTimeMillis(), new Notification(User.builder().email("banana@naver.com").nickName("바나나").build(), NotificationType.NEW_GUESTBOOK, "새로운 방명록이 등로되었습니다.", "url", false));

        //when
        emitterRepository.deleteAllEventCacheStartWithId(String.valueOf(userId));

        //then
        Assertions.assertEquals(0, emitterRepository.findAllEventCacheStartWithByUserId(String.valueOf(userId)).size());
    }
}