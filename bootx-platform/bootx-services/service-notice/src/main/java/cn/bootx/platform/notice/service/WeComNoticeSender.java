package cn.bootx.platform.notice.service;

import cn.bootx.platform.notice.core.wecom.entity.WeComNoticeReceive;
import cn.bootx.platform.notice.core.wecom.entity.msg.*;

import java.io.InputStream;

/**
 * 企业微信通知服务
 *
 * @author xxm
 * @since 2022/7/16
 */
public interface WeComNoticeSender {

    /**
     * 发送文本消息
     */
    String sendTextNotice(WeComTextMsg msg, WeComNoticeReceive receive);

    /**
     * 发送图片消息
     */
    String sendImageNotice(WeComImageMsg msg, WeComNoticeReceive receive);

    /**
     * 发送图片消息 (传入文件)
     */
    String sendImageNotice(InputStream inputStream, WeComNoticeReceive receive);

    /**
     * 发送语音消息
     */
    String sendVoiceNotice(WeComVoiceMsg msg, WeComNoticeReceive receive);

    /**
     * 发送语音消息(传入文件)
     */
    String sendVoiceNotice(InputStream inputStream, WeComNoticeReceive receive);

    /**
     * 发送视频消息 (传入文件)
     */
    String sendVideoNotice(String title, String description, InputStream inputStream, WeComNoticeReceive receive);

    /**
     * 发送视频消息
     */
    String sendVideoNotice(WeComVideoMsg msg, WeComNoticeReceive receive);

    /**
     * 发送文本卡片消息
     */
    String sendTextCardNotice(WeComTextCardMsg msg, WeComNoticeReceive receive);

    /**
     * 发送图文消息
     */
    String sendNewsNotice(WeComNewsMsg msg, WeComNoticeReceive receive);

    /**
     * 发送图文消息(mpnews)
     */
    String sendMpNewsNotice(WeComMpNewsMsg msg, WeComNoticeReceive receive);

    /**
     * 发送markdown消息
     */
    String sendMarkdownNotice(WeComMarkdownMsg msg, WeComNoticeReceive receive);

    /**
     * 撤回企微消息
     */
    void recallNotice(String msgId);

}
