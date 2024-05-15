package cn.bootx.platform.notice.service;

import me.chanjar.weixin.cp.bean.article.NewArticle;

import java.io.InputStream;
import java.util.List;

/**
 * 企业微信机器人配置
 *
 * @author xxm
 * @since 2022/7/16
 */
public interface WeComRobotNoticeSender {

    /**
     * 发送文本消息
     */
    void sendTextNotice(String code, String content, List<String> mentionedList, List<String> mobileList);

    /**
     * 发送markdown消息
     */
    void sendMarkdownNotice(String code, String content);

    /**
     * 发送图片消息
     */
    void sendImageNotice(String code, String imageBase64, String md5);

    /**
     * 发送图片消息
     */

    void sendImageNotice(String code, InputStream imageIs);

    /**
     * 发送图文消息
     */
    void sendNewsNotice(String code, List<NewArticle> articleList);

    /**
     * 发送文件消息
     */
    void sendFIleNotice(String code, String mediaId);

    /**
     * 发送文件消息
     */
    void sendFIleNotice(String code, InputStream fileIs);

    /**
     * 发送文件消息
     */
    void sendFIleNotice(String code, InputStream inputStream, String filename);

}
