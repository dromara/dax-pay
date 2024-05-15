package cn.bootx.platform.starter.wecom.core.robot.service;

import cn.bootx.platform.starter.wecom.core.robot.executor.RobotMediaFileUploadRequestExecutor;
import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.starter.wecom.core.robot.dao.WecomRobotConfigManager;
import cn.bootx.platform.starter.wecom.core.robot.domin.UploadMedia;
import cn.bootx.platform.starter.wecom.core.robot.entity.WecomRobotConfig;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.cp.api.WxCpGroupRobotService;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.article.NewArticle;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import static cn.bootx.platform.starter.wecom.code.WeComCode.ROBOT_UPLOAD_URL;

/**
 * 企微机器人消息通知
 *
 * @author xxm
 * @since 2022/7/23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeComRobotNoticeService {

    private final WxCpService wxCpService;

    private final WecomRobotConfigManager robotConfigManager;

    /**
     * 发送文本消息
     */
    @SneakyThrows
    public void sendTextNotice(String code, String content, List<String> mentionedList, List<String> mobileList) {
        WecomRobotConfig robotConfig = robotConfigManager.findByCode(code)
            .orElseThrow(() -> new DataNotExistException("企业微信机器人配置未找到"));
        WxCpGroupRobotService robotService = wxCpService.getGroupRobotService();
        robotService.sendText(robotConfig.toWebhookUrl(), content, mentionedList, mobileList);
    }

    /**
     * 发送markdown消息
     */
    @SneakyThrows
    public void sendMarkdownNotice(String code, String content) {
        WecomRobotConfig robotConfig = robotConfigManager.findByCode(code)
            .orElseThrow(() -> new DataNotExistException("企业微信机器人配置未找到"));
        WxCpGroupRobotService robotService = wxCpService.getGroupRobotService();
        robotService.sendMarkdown(robotConfig.toWebhookUrl(), content);
    }

    /**
     * 发送图片消息
     */
    @SneakyThrows
    public void sendImageNotice(String code, String imageBase64, String md5) {
        WecomRobotConfig robotConfig = robotConfigManager.findByCode(code)
            .orElseThrow(() -> new DataNotExistException("企业微信机器人配置未找到"));
        WxCpGroupRobotService robotService = wxCpService.getGroupRobotService();
        robotService.sendImage(robotConfig.toWebhookUrl(), imageBase64, md5);
    }

    /**
     * 发送图文消息
     */
    @SneakyThrows
    public void sendNewsNotice(String code, List<NewArticle> articleList) {
        WecomRobotConfig robotConfig = robotConfigManager.findByCode(code)
            .orElseThrow(() -> new DataNotExistException("企业微信机器人配置未找到"));
        WxCpGroupRobotService robotService = wxCpService.getGroupRobotService();
        robotService.sendNews(robotConfig.toWebhookUrl(), articleList);
    }

    /**
     * 发送文件消息
     */
    @SneakyThrows
    public void sendFIleNotice(String code, String mediaId) {
        WecomRobotConfig robotConfig = robotConfigManager.findByCode(code)
            .orElseThrow(() -> new DataNotExistException("企业微信机器人配置未找到"));
        WxCpGroupRobotService robotService = wxCpService.getGroupRobotService();
        robotService.sendFile(robotConfig.toWebhookUrl(), mediaId);
    }

    /**
     * 机器人临时文件上传
     */
    @SneakyThrows
    public String updatedMedia(String code, InputStream inputStream) {
        byte[] bytes = IoUtil.readBytes(inputStream);
        String fileType = FileTypeUtil.getType(new ByteArrayInputStream(bytes));
        UploadMedia uploadMedia = new UploadMedia().setFileType(fileType)
            .setFilename(IdUtil.getSnowflakeNextIdStr())
            .setInputStream(new ByteArrayInputStream(bytes));
        WecomRobotConfig robotConfig = robotConfigManager.findByCode(code)
            .orElseThrow(() -> new DataNotExistException("企业微信机器人配置未找到"));
        String url = StrUtil.format(ROBOT_UPLOAD_URL, robotConfig.getWebhookKey());
        WxMediaUploadResult result = wxCpService.execute(new RobotMediaFileUploadRequestExecutor(), url, uploadMedia);
        return result.getMediaId();
    }

    /**
     * 机器人临时文件上传
     */
    @SneakyThrows
    public String updatedMedia(String code, InputStream inputStream, String filename) {
        byte[] bytes = IoUtil.readBytes(inputStream);
        String fileType = FileTypeUtil.getType(new ByteArrayInputStream(bytes), filename);
        UploadMedia uploadMedia = new UploadMedia().setFileType(fileType)
            .setFilename(FileNameUtil.mainName(filename))
            .setInputStream(new ByteArrayInputStream(bytes));
        WecomRobotConfig robotConfig = robotConfigManager.findByCode(code)
            .orElseThrow(() -> new DataNotExistException("企业微信机器人配置未找到"));
        String url = StrUtil.format(ROBOT_UPLOAD_URL, robotConfig.getWebhookKey());
        WxMediaUploadResult result = wxCpService.execute(new RobotMediaFileUploadRequestExecutor(), url, uploadMedia);
        return result.getMediaId();
    }

}
