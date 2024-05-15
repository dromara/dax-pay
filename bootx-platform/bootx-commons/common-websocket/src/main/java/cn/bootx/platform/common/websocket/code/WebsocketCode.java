package cn.bootx.platform.common.websocket.code;

/**
 * websocket常量码
 *
 * @author xxm
 * @since 2022/6/9
 */
public interface WebsocketCode {

    /* 通知类型 */
    // 普通通知
    int NOTIFICATION_INFO = 1001;

    // 警告通知
    int NOTIFICATION_WARN = 1002;

    // 错误通知
    int NOTIFICATION_ERROR = 1003;

    // 事件通知跳转
    int EVENT_NOTICE = 9001;

}
