package cn.bootx.platform.notice.service;

import cn.bootx.platform.notice.core.dingtalk.entity.corp.DingCorpNoticeReceive;
import cn.bootx.platform.notice.core.dingtalk.entity.corp.DingCorpNoticeUpdate;
import cn.bootx.platform.notice.core.dingtalk.entity.msg.*;

import java.io.InputStream;

/**
 * 钉钉消息通知服务
 *
 * @author xxm
 * @since 2022/7/16
 */
public interface DingTalkNoticeSender {

    /**
     * 发送文本消息
     * @return 发布消息任务ID
     */
    Long sendTextCorpNotice(DingTextMsg dingTextMsg, DingCorpNoticeReceive receive);

    /**
     * 发送图片消息
     * @return 发布消息任务ID
     */
    Long sendImageCorpNotice(DingImageMsg dingImageMsg, DingCorpNoticeReceive receive);

    /**
     * 发送图片消息 (文件方式)
     * @return 发布消息任务ID
     */
    Long sendImageCorpNotice(InputStream inputStream, DingCorpNoticeReceive receive);

    /**
     * 发送图片消息 (文件方式)
     * @return 发布消息任务ID
     */
    Long sendImageCorpNotice(InputStream inputStream, String filename, DingCorpNoticeReceive receive);

    /**
     * 发送语音消息
     * @return 发布消息任务ID
     */
    Long sendVoiceCorpNotice(DingVoiceMsg dingVoiceMsg, DingCorpNoticeReceive receive);

    /**
     * 发送语音消息 (文件)
     * @return 发布消息任务ID
     */
    Long sendVoiceCorpNotice(InputStream inputStream, DingCorpNoticeReceive receive);

    /**
     * 发送语音消息 (文件)
     * @return 发布消息任务ID
     */
    Long sendVoiceCorpNotice(InputStream inputStream, String filename, DingCorpNoticeReceive receive);

    /**
     * 发送文件消息
     * @return 发布消息任务ID
     */
    Long sendFileCorpNotice(DingFileMsg dingFileMsg, DingCorpNoticeReceive receive);

    /**
     * 发送文件消息(文件)
     * @return 发布消息任务ID
     */
    Long sendFileCorpNotice(InputStream inputStream, DingCorpNoticeReceive receive);

    /**
     * 发送文件消息(文件)
     * @return 发布消息任务ID
     */
    Long sendFileCorpNotice(InputStream inputStream, String filename, DingCorpNoticeReceive receive);

    /**
     * 发送链接消息
     * @return 发布消息任务ID
     */
    Long sendLinkCorpNotice(DingLinkMsg dingLinkMsg, DingCorpNoticeReceive receive);

    /**
     * 发送OA消息
     * @return 发布消息任务ID
     */
    Long sendOaCorpNotice(DingOaMsg dingOaMsg, DingCorpNoticeReceive receive);

    /**
     * 发送markdown消息
     */
    Long sendMarkDownCorpNotice(DingMarkDownMsg dingMarkDownMsg, DingCorpNoticeReceive receive);

    /**
     * 发送卡片消息
     */
    Long sendActionCardCorpNotice(DingActionCardMsg dingActionCardMsg, DingCorpNoticeReceive receive);

    /**
     * 更新OA工作通知消息
     */
    void updateOaCorpNotice(DingCorpNoticeUpdate updateCorpNotice);

    /**
     * 撤回工作通知消息
     */
    void recallCorpNotice(Long msgTaskId);

    /**
     * 发送文本企业群消息
     * @return 发布企业群消息ID
     */
    String sendTextChatNotice(DingTextMsg dingTextMsg, String chatId);

    /**
     * 发送图片企业群消息
     * @return 发布企业群消息ID
     */
    String sendImageChatNotice(DingImageMsg dingImageMsg, String chatId);

    /**
     * 发送语音企业群消息
     * @return 发布企业群消息ID
     */
    String sendVoiceChatNotice(DingVoiceMsg dingVoiceMsg, String chatId);

    /**
     * 发送文件企业群消息
     * @return 发布企业群消息ID
     */
    String sendFileChatNotice(DingFileMsg dingFileMsg, String chatId);

    /**
     * 发送链接企业群消息
     * @return 发布企业群消息ID
     */
    String sendLinkChatNotice(DingLinkMsg dingLinkMsg, String chatId);

    /**
     * 发送OA企业群消息
     * @return 发布企业群消息ID
     */
    String sendOaChatNotice(DingOaMsg dingOaMsg, String chatId);

    /**
     * 发送markdown企业群消息
     */
    String sendMarkDownChatNotice(DingMarkDownMsg dingMarkDownMsg, String chatId);

    /**
     * 发送卡片企业群消息
     */
    String sendActionCardChatNotice(DingActionCardMsg dingActionCardMsg, String chatId);

}
