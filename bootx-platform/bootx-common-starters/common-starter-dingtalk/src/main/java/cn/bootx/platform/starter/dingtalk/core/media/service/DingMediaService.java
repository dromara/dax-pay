package cn.bootx.platform.starter.dingtalk.core.media.service;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.common.core.util.FileUtil;
import cn.bootx.platform.common.jackson.util.JacksonUtil;
import cn.bootx.platform.starter.dingtalk.code.DingTalkCode;
import cn.bootx.platform.starter.dingtalk.core.base.result.MediaResult;
import cn.bootx.platform.starter.dingtalk.core.base.service.DingAccessService;
import cn.bootx.platform.starter.dingtalk.core.media.dao.DingMediaMd5Manager;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Objects;

import static cn.bootx.platform.starter.dingtalk.code.DingTalkCode.*;

/**
 * 钉钉媒体文件管理
 *
 * @author xxm
 * @since 2022/7/25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DingMediaService {

    private final DingAccessService dingAccessService;

    private final DingMediaMd5Manager dingMediaMd5Manager;

    /**
     * 文件上传
     */
    @SneakyThrows
    public String uploadMedia(InputStream inputStream, String fileName, String mediaType) {
        // 判断md5是否是已经上传过的, 上传过的直接使用媒体id( 发现钉钉媒体id只能使用一次, 和文档描述不一致 )
        byte[] bytes = IoUtil.readBytes(inputStream);
        String filePrefix = FileNameUtil.mainName(fileName);
        String fileType = FileTypeUtil.getType(new ByteArrayInputStream(bytes), fileName);
        File tmpFile = FileUtil.createTempFile(new ByteArrayInputStream(bytes), filePrefix, fileType);
        String body = HttpUtil
            .createPost(StrUtil.format(DingTalkCode.MEDIA_UPLOAD_URL, dingAccessService.getAccessToken()))
            .form(MEDIA, tmpFile, fileName)
            .form(TYPE, mediaType)
            .execute()
            .body();
        MediaResult mediaResult = JacksonUtil.toBean(body, MediaResult.class);
        if (!Objects.equals(mediaResult.getCode(), SUCCESS_CODE)) {
            throw new BizException(mediaResult.getMsg());
        }
        String mediaId = mediaResult.getMediaId();
        // dingMediaMd5Manager.save(new DingMediaMd5(mediaId,md5));
        return mediaId;
    }

    /**
     * 文件上传
     */
    @SneakyThrows
    public String uploadMedia(InputStream inputStream, String mediaType) {
        byte[] bytes = IoUtil.readBytes(inputStream);

        String fileType = FileTypeUtil.getType(new ByteArrayInputStream(bytes));
        File tmpFile = FileUtil.createTempFile(new ByteArrayInputStream(bytes), IdUtil.getSnowflakeNextIdStr(),
                fileType);
        String body = HttpUtil
            .createPost(StrUtil.format(DingTalkCode.MEDIA_UPLOAD_URL, dingAccessService.getAccessToken()))
            .form(MEDIA, tmpFile)
            .form(TYPE, mediaType)
            .execute()
            .body();
        MediaResult mediaResult = JacksonUtil.toBean(body, MediaResult.class);
        if (!Objects.equals(mediaResult.getCode(), SUCCESS_CODE)) {
            throw new BizException(mediaResult.getMsg());
        }
        return mediaResult.getMediaId();
    }

}
