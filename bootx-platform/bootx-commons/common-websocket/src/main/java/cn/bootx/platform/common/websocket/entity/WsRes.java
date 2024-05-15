package cn.bootx.platform.common.websocket.entity;

import cn.bootx.platform.common.websocket.code.WebsocketCode;
import lombok.experimental.UtilityClass;

/**
 * websocket 响应工具类
 *
 * @author xxm
 * @since 2022/6/9
 */
@UtilityClass
public class WsRes {

    /**
     * 消息通知(普通)
     */
    public <T> WsResult<T> notificationInfo(T data) {
        return new WsResult<>(WebsocketCode.NOTIFICATION_INFO, data);
    }

    /**
     * 消息通知(警告)
     */
    public <T> WsResult<T> notificationWarn(T data) {
        return new WsResult<>(WebsocketCode.NOTIFICATION_WARN, data);
    }

    /**
     * 消息通知(错误)
     */
    public <T> WsResult<T> notificationError(T data) {
        return new WsResult<>(WebsocketCode.NOTIFICATION_ERROR, data);
    }

    /**
     * 事件通知
     */
    public <T> WsResult<T> eventNotice(T data, String code) {
        return new WsResult<>(WebsocketCode.EVENT_NOTICE, data, code);
    }

    /**
     * 事件通知
     */
    public <T> WsResult<T> eventNotice(String code) {
        return new WsResult<>(WebsocketCode.EVENT_NOTICE, null, code);
    }

}
