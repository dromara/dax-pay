package cn.bootx.platform.starter.file.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.starter.file.UploadFileParam;
import cn.bootx.platform.starter.file.configuration.FileUploadProperties;
import cn.bootx.platform.starter.file.convert.FileConvert;
import cn.bootx.platform.starter.file.dao.UploadFileManager;
import cn.bootx.platform.starter.file.dto.UploadFileDto;
import cn.bootx.platform.starter.file.entity.UploadFileInfo;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.dromara.x.file.storage.core.UploadPretreatment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.net.URLEncoder;

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
    public PageResult<UploadFileDto> page(PageParam pageParam, UploadFileParam param) {
        return MpUtil.convert2DtoPageResult(uploadFileManager.page(pageParam,param));
    }

    /**
     * 获取单条详情
     */
    public UploadFileDto findById(Long id){
        return uploadFileManager.findById(id)
                .map(UploadFileInfo::toDto)
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
    public UploadFileDto upload(MultipartFile file, String fileName) {
        UploadPretreatment uploadPretreatment = fileStorageService.of(file);
        if (StrUtil.isNotBlank(fileName)){
            uploadPretreatment.setOriginalFilename(fileName);
        }
        FileInfo upload =uploadPretreatment.upload();
        return FileConvert.CONVERT.toDto(upload);
    }

    /**
     * 浏览
     */
    @SneakyThrows
    public void preview(Long id, HttpServletResponse response) {
        FileInfo info = fileStorageService.getFileInfoByUrl(String.valueOf(id));
        byte[] bytes = fileStorageService.download(info).bytes();
        val is = new ByteArrayInputStream(bytes);
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
        headers.setContentDispositionFormData("attachment", URLEncoder.encode(fileName, CharsetUtil.UTF_8));
        return new ResponseEntity<>(bytes,headers,HttpStatus.OK);
    }

    /**
     * 获取文件预览地址
     */
    public String getFilePreviewUrl(Long id) {
        if (fileUploadProperties.isServiceProxy()){
            return this.getServerUrl() + "/file/preview/" + id;
        } else {
            return "";
        }
    }

    /**
     * 获取文件预览地址前缀
     */
    public String getFilePreviewUrlPrefix() {
        return this.getServerUrl() + "/file/preview/";
    }

    /**
     * 获取文件地址
     */
    public String getFileDownloadUrl(Long id) {
        if (fileUploadProperties.isServiceProxy()){
            return this.getServerUrl() + "/file/download/" + id;
        } else {
            return "";
        }
    }

    /**
     * 服务地址
     */
    private String getServerUrl() {
        return fileUploadProperties.getServerUrl();
    }
}
