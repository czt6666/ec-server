package com.bistu.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Slf4j
public class SSEUtil {
    private static final Map<String, SseEmitter> sseEmitterMap = new ConcurrentHashMap<>();

    public static SseEmitter createSse(String uid, boolean reTry) {
        //默认30秒超时,设置为0L则永不超时
        SseEmitter sseEmitter = new SseEmitter(0L);
        //完成后回调
        sseEmitter.onCompletion(() -> {
            log.info("[{}]结束连接...................", uid);
            sseEmitterMap.remove(uid);
        });
        //超时回调
        sseEmitter.onTimeout(() -> {
            log.info("[{}]连接超时...................", uid);
        });
        //异常回调
        sseEmitter.onError(
                throwable -> {
                    try {
                        log.info("[{}]连接异常,{}", uid, throwable.toString());
                        if (reTry) {
                            sseEmitter.send(SseEmitter.event()
                                    .id(uid)
                                    .name("发生异常！")
                                    .data("发生异常请重试！")
                                    .reconnectTime(3000));
                            sseEmitterMap.put(uid, sseEmitter);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
        try {
            if (reTry) {
                sseEmitter.send(SseEmitter.event().reconnectTime(60 * 1000));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        sseEmitterMap.put(uid, sseEmitter);
        log.info("[{}]创建sse连接成功！", uid);
        return sseEmitter;
    }

    public static boolean sendMessage(String uid,String messageId, String message, boolean reTry) {
        if (message.isEmpty()) {
            log.info("[{}]：参数异常，msg为null", uid);
            return false;
        }
        SseEmitter sseEmitter = sseEmitterMap.get(uid);
        if (sseEmitter == null) {
            log.error("消息推送失败uid:[{}],没有创建连接，请重试。", uid);
            return false;
        }
        try {
            if (reTry) {
                sseEmitter.send(SseEmitter.event().id(messageId).reconnectTime(60 * 1000L).data(message));
            } else {
                sseEmitter.send(SseEmitter.event().id(messageId).data(message));
            }
            log.error("用户{},消息id:{},推送成功", uid,messageId);
            return true;
        }catch (Exception e) {
            sseEmitterMap.remove(uid);
            log.error("用户{},消息id:{},推送异常:{}", uid,messageId, e.getMessage());
            sseEmitter.complete();
            return false;
        }
    }

    public static SseEmitter getEmitter(String uid) {
	    return sseEmitterMap.get(uid);
    }

    public static void closeSse(String uid){
        if (sseEmitterMap.containsKey(uid)) {
            SseEmitter sseEmitter = sseEmitterMap.get(uid);
            sseEmitter.complete();
            sseEmitterMap.remove(uid);
        }else {
            log.info("用户{} 连接已关闭",uid);
        }

    }
}


