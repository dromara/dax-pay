package cn.bootx.platform.notice.service;

import cn.bootx.platform.starter.dingtalk.param.notice.msg.LinkMsg;
import cn.bootx.platform.starter.dingtalk.param.notice.msg.MarkdownMsg;
import cn.bootx.platform.starter.dingtalk.param.notice.msg.TextMsg;

/**
 * 钉钉机器人消息通知
 *
 * @author xxm
 * @since 2022/7/16
 */
public interface DingRobotNoticeSender {

    /**
     * 简单文本消息发送
     */
    void sendSimpleText(String code, String msg);

    /**
     * 文本消息发送
     */
    void sendText(String code, TextMsg dingTalkTextNotice);

    /**
     * 发送链接消息
     */
    void sendLink(String code, LinkMsg notice);

    /**
     * 发送Markdown消息
     */
    void sendMarkdown(String code, MarkdownMsg notice);

}
