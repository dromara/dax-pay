package cn.bootx.platform.notice.core.wecom.service;

import cn.bootx.platform.notice.service.WeComRobotNoticeSender;
import cn.bootx.platform.starter.wecom.core.robot.service.WeComRobotNoticeService;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.IoUtil;
import cn.hutool.crypto.digest.DigestUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.cp.bean.article.NewArticle;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

/**
 * 微信机器人消息发送
 *
 * @author xxm
 * @since 2022/7/23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeComRobotNoticeSenderImpl implements WeComRobotNoticeSender {

    private final WeComRobotNoticeService robotNoticeService;

    /**
     * 发送文本消息
     */
    @Override
    public void sendTextNotice(String code, String content, List<String> mentionedList, List<String> mobileList) {
        robotNoticeService.sendTextNotice(code, content, mentionedList, mobileList);
    }

    /**
     * 发送markdown消息
     */
    @Override
    public void sendMarkdownNotice(String code, String content) {
        robotNoticeService.sendMarkdownNotice(code, content);
    }

    /**
     * 发送图片消息
     */
    @Override
    public void sendImageNotice(String code, String imageBase64, String md5) {
        robotNoticeService.sendImageNotice(code, imageBase64, md5);
    }

    /**
     * 发送图片消息
     */
    @Override
    public void sendImageNotice(String code, InputStream imageIs) {
        byte[] bytes = IoUtil.readBytes(imageIs);
        String md5 = DigestUtil.md5Hex(bytes);
        String imageBase64 = Base64.encode(bytes);
        robotNoticeService.sendImageNotice(code, imageBase64, md5);
    }

    /**
     * 发送图文消息
     */
    @Override
    public void sendNewsNotice(String code, List<NewArticle> articleList) {
        robotNoticeService.sendNewsNotice(code, articleList);
    }

    /**
     * 发送文件消息
     */
    @Override
    public void sendFIleNotice(String code, String mediaId) {
        robotNoticeService.sendFIleNotice(code, mediaId);
    }

    /**
     * 发送文件消息
     */
    @SneakyThrows
    @Override
    public void sendFIleNotice(String code, InputStream fileIs) {
        String mediaId = robotNoticeService.updatedMedia(code, fileIs);
        robotNoticeService.sendFIleNotice(code, mediaId);
    }

    /**
     * 发送文件消息
     */
    @SneakyThrows
    @Override
    public void sendFIleNotice(String code, InputStream inputStream, String filename) {
        String mediaId = robotNoticeService.updatedMedia(code, inputStream, filename);
        robotNoticeService.sendFIleNotice(code, mediaId);
    }

}
