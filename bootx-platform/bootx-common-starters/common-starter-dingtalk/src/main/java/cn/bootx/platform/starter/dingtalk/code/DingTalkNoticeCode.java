package cn.bootx.platform.starter.dingtalk.code;

/**
 * 钉钉通知消息常量
 *
 * @author xxm
 * @since 2022/4/2
 */
public interface DingTalkNoticeCode {

    /* 消息类型 */
    /** 文本消息 */
    String MSG_TEXT = "text";

    /** 图片消息 */
    String MSG_IMAGE = "image";

    /** 语音消息 */
    String MSG_VOICE = "voice";

    /** 文件消息 */
    String MSG_FILE = "file";

    /** 链接消息 */
    String MSG_LINK = "link";

    /** OA消息 */
    String MSG_OA = "oa";

    /** markdown消息 */
    String MSG_MARKDOWN = "markdown";

    /** 卡片消息 */
    String MSG_ACTION_CARD = "action_card";

}
