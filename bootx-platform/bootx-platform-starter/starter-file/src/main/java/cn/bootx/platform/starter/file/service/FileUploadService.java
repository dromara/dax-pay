package cn.bootx.platform.starter.file.service;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.BizException;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.starter.file.convert.FileConvert;
import cn.bootx.platform.starter.file.dao.UploadFileManager;
import cn.bootx.platform.starter.file.entity.UploadFileInfo;
import cn.bootx.platform.starter.file.param.FileUploadRequestParams;
import cn.bootx.platform.starter.file.param.UploadFileInfoParam;
import cn.bootx.platform.starter.file.param.UploadFileQuery;
import cn.bootx.platform.starter.file.result.FileUploadParamsResult;
import cn.bootx.platform.starter.file.result.UploadFileResult;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.dromara.x.file.storage.core.constant.Constant;
import org.dromara.x.file.storage.core.presigned.GeneratePresignedUrlResult;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;

/**
 * 文件上传管理类
 *
 * @author xxm
 * @since 2022/1/14
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileUploadService {
    private final UploadFileManager uploadFileManager;
    private final FileStorageService fileStorageService;

    /**
     * 分页
     */
    public PageResult<UploadFileResult> page(PageParam pageParam, UploadFileQuery param) {
        return MpUtil.toPageResult(uploadFileManager.page(pageParam, param));
    }

    /**
     * 获取单条详情
     */
    public UploadFileResult findByUrl(Long id) {
        return uploadFileManager.findById(id)
                .map(UploadFileInfo::toResult)
                .orElseThrow(DataNotExistException::new);
    }

    /**
     * 根据URL获取单条详情
     */
    public UploadFileResult findByUrl(String url) {
        return uploadFileManager.findByUrl(url)
                .map(UploadFileInfo::toResult)
                .orElseThrow(DataNotExistException::new);
    }

    /**
     * 文件删除
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        UploadFileInfo uploadFileInfo = uploadFileManager.findById(id)
                .orElseThrow(DataNotExistException::new);
        fileStorageService.delete(FileConvert.CONVERT.toFileInfo(uploadFileInfo));
    }

    /**
     * 根据文件名称下载
     */
    public byte[] download(String attachName) {
        String ossFileUrl = this.getOssFileUrl(attachName);
        return HttpUtil.downloadBytes(ossFileUrl);
    }

    /**
     * 根据存储的文件对象下载
     */
    public byte[] download(FileInfo fileInfo) {
        return HttpUtil.downloadBytes(this.getOssFileUrl(fileInfo.getUrl()));
    }

    /**
     * 文件下载, 首先判断文件是否存在, 用在严格的场景中
     */
    public byte[] downloadAndCheck(String attachName) {
        FileInfo fileInfo = fileStorageService.getFileInfoByUrl(attachName);
        if (fileInfo == null){
            throw new DataNotExistException("文件不存在");
        }
        return HttpUtil.downloadBytes(this.getOssFileUrl(attachName));
    }

    /**
     * 获取直传上传参数
     */
    public FileUploadParamsResult getUploadParams(FileUploadRequestParams params) {
        String fileExtension = FileUtil.extName(params.getFileName());
        String attachName = IdUtil.fastSimpleUUID() + (StringUtils.isNotBlank(fileExtension) ? "." + fileExtension : "");
        GeneratePresignedUrlResult uploadResult = fileStorageService
                .generatePresignedUrl()
                .setFilename(attachName) // 设置保存的文件名
                .setMethod(Constant.GeneratePresignedUrl.Method.PUT)    // 签名方法
                .setExpiration(DateUtil.offsetMinute(new Date(), 10)) // 过期时间 10 分钟
                .putHeaders(Constant.Metadata.CONTENT_TYPE, params.getMediaType())
                .putHeaders(Constant.Metadata.CONTENT_LENGTH, String.valueOf(params.getFileSize()))
                .generatePresignedUrl();
        FileUploadParamsResult result = new FileUploadParamsResult();
        result.setUrl(uploadResult.getUrl())
                .setAttachName(attachName)
                .setHeaders(uploadResult.getHeaders());
        return result;
    }

    /**
     * 保存前端直传的文件信息
     */
    public void saveFileInfo(UploadFileInfoParam param) {
        UploadFileInfo uploadFileInfo = FileConvert.CONVERT.convert(param);
        // 扩展名
        String fileExtension = FileUtil.extName(uploadFileInfo.getOriginalFilename());
        uploadFileInfo.setExt(fileExtension);
        // 平台
        String defaultPlatform = fileStorageService.getProperties()
                .getDefaultPlatform();
        uploadFileInfo.setPlatform(defaultPlatform);
        uploadFileManager.save(uploadFileInfo);
    }

    /**
     * 获取oss直传文件下载链接
     */
    public String getOssFileUrl(String attachName) {
        // 系统中旧数据存储的地址是/开头, 为了兼容旧数据使用oss生成预签名链接的时候需要去掉
        // 新数据存储格式就没有/开头
        attachName = StrUtil.removePrefix(attachName, "/");
        GeneratePresignedUrlResult downloadResult = fileStorageService
                .generatePresignedUrl()
                .setFilename(attachName) // 文件名
                .setMethod(Constant.GeneratePresignedUrl.Method.GET) // 签名方法
                .setExpiration(DateUtil.offsetMinute(new Date(), 10)) // 过期时间 10 分钟
                .putResponseHeaders(
                        Constant.Metadata.CONTENT_DISPOSITION, "attachment;filename=" + attachName)
                .generatePresignedUrl();
        return downloadResult.getUrl();
    }

    /**
     * 前端直传文件下载/预览
     */
    public void ossDownload(HttpServletResponse httpServletResponse, String attachName) {
        try {
            httpServletResponse.sendRedirect(this.getOssFileUrl(attachName));
        } catch (IOException e) {
            log.error("下载文件失败", e);
            httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
            throw new BizException(e.getMessage());
        }
    }


}
