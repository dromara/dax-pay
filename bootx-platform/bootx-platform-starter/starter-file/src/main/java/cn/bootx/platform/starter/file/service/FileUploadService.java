package cn.bootx.platform.starter.file.service;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.starter.file.configuration.FileUploadProperties;
import cn.bootx.platform.starter.file.convert.FileConvert;
import cn.bootx.platform.starter.file.dao.UploadFileManager;
import cn.bootx.platform.starter.file.entity.UploadFileInfo;
import cn.bootx.platform.starter.file.param.UploadFileQuery;
import cn.bootx.platform.starter.file.result.UploadFileResult;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.dromara.x.file.storage.core.upload.UploadPretreatment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

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
    private final FileUploadProperties fileUploadProperties;

    /**
     * 分页
     */
    public PageResult<UploadFileResult> page(PageParam pageParam, UploadFileQuery param) {
        return MpUtil.toPageResult(uploadFileManager.page(pageParam,param));
    }

    /**
     * 获取单条详情
     */
    public UploadFileResult findById(Long id){
        return uploadFileManager.findById(id)
                .map(UploadFileInfo::toResult)
               .orElseThrow(DataNotExistException::new);
    }

    /**
     * 根据URL获取单条详情
     */
    public UploadFileResult findById(String url){
        return uploadFileManager.findByUrl(url)
                .map(UploadFileInfo::toResult)
               .orElseThrow(DataNotExistException::new);
    }

    /**
     * 文件删除
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id){
        UploadFileInfo uploadFileInfo = uploadFileManager.findById(id)
                .orElseThrow(DataNotExistException::new);
        fileStorageService.delete(FileConvert.CONVERT.toFileInfo(uploadFileInfo));
    }

    /**
     * 文件上传
     * @param file 文件
     * @param fileName 文件名称
     */
    @Transactional(rollbackFor = Exception.class)
    public UploadFileResult upload(@RequestPart MultipartFile file, String fileName) {
        UploadPretreatment uploadPretreatment = fileStorageService.of(file);
        if (StrUtil.isNotBlank(fileName)){
            uploadPretreatment.setOriginalFilename(fileName);
        }
        // 按年月日进行分目录, 因为目录拼接的情况, 所以开头不可以为 /
        uploadPretreatment.setPath(LocalDateTimeUtil.format(LocalDateTime.now(), "yyyy/MM/dd/"));

        FileInfo upload = uploadPretreatment.upload();
        return FileConvert.CONVERT.toResult(upload);
    }

    /**
     * 文件预览
     */
    @SneakyThrows
    public void preview(Long id, HttpServletResponse response) {
        FileInfo info = fileStorageService.getFileInfoByUrl(String.valueOf(id));
        if (info == null){
            log.warn("文件不存在");
            return;
        }
        byte[] bytes = fileStorageService.download(info).bytes();
        var is = new ByteArrayInputStream(bytes);
        // 获取响应输出流
        ServletOutputStream os = response.getOutputStream();
        IoUtil.copy(is, os);
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, info.getContentType());
        IoUtil.close(is);
        IoUtil.close(os);
    }

    /**
     * 文件下载
     */
    @SneakyThrows
    public ResponseEntity<byte[]> download(Long id) {
        FileInfo fileInfo = fileStorageService.getFileInfoByUrl(String.valueOf(id));
        byte[] bytes = fileStorageService.download(fileInfo).bytes();
        // 设置header信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        String fileName = fileInfo.getOriginalFilename();
        headers.setContentDispositionFormData("attachment", URLEncoder.encode(fileName, StandardCharsets.UTF_8));
        return new ResponseEntity<>(bytes,headers,HttpStatus.OK);
    }

    /**
     * 文件访问转发地址(当前后端服务地址或被代理后的地址), 流量会经过后端服务的转发
     */
    public String getServerFilePreviewUrlPrefix() {
        return this.getForwardServerUrl() + "/file/preview/";
    }

    /**
     * 文件访问转发地址(当前后端服务地址或被代理后的地址), 流量会经过后端服务的转发
     */
    private String getForwardServerUrl() {
        return fileUploadProperties.getForwardServerUrl();
    }
}
