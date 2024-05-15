package cn.bootx.platform.notice.code;

/**
 * 站内信编码
 *
 * @author xxm
 * @since 2022/8/14
 */
public interface SiteMessageCode {

    /* 接收类型 */
    /** 指定用户 */
    String RECEIVE_USER = "user";

    /** 全部用户 */
    String RECEIVE_ALL = "all";

    /* 发布状态 */
    /** 草稿 */
    String STATE_DRAFT = "draft";

    /** 已发送 */
    String STATE_SENT = "sent";

    /** 撤销 */
    String STATE_CANCEL = "cancel";

    /* 事件 */
    /** 消息更新 */
    String EVENT_MESSAGE_UPDATE = "notice_message_update";

}
