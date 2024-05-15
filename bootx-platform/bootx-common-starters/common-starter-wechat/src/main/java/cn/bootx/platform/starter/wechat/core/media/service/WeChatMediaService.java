package cn.bootx.platform.starter.wechat.core.media.service;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.core.util.FileUtil;
import cn.bootx.platform.starter.wechat.dto.media.WeChatMediaDto;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileNameUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpMaterialService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.material.WxMpMaterial;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialNewsBatchGetResult.WxMaterialNewsBatchGetNewsItem;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 素材管理
 *
 * @author xxm
 * @since 2022/8/9
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatMediaService {

    private final WxMpService wxMpService;

    /**
     * 分页查询
     */
    @SneakyThrows
    public PageResult<WeChatMediaDto> pageFile(PageParam pageParam, String type) {
        WxMpMaterialService materialService = wxMpService.getMaterialService();
        val result = materialService.materialFileBatchGet(type, pageParam.start(), pageParam.getSize());
        // val result = new WxMpMaterialFileBatchGetResult();
        val items = result.getItems().stream().map(WeChatMediaDto::init).collect(Collectors.toList());
        PageResult<WeChatMediaDto> pageResult = new PageResult<>();
        pageResult.setCurrent(pageParam.getCurrent())
            .setRecords(items)
            .setSize(pageParam.getSize())
            .setTotal(result.getTotalCount());
        return pageResult;
    }

    /**
     * 分页查询(图文)
     */
    @SneakyThrows
    public PageResult<WxMaterialNewsBatchGetNewsItem> pageNews(PageParam pageParam) {
        WxMpMaterialService materialService = wxMpService.getMaterialService();
        val result = materialService.materialNewsBatchGet(pageParam.start(), pageParam.getSize());
        val items = result.getItems();
        PageResult<WxMaterialNewsBatchGetNewsItem> pageResult = new PageResult<>();
        pageResult.setCurrent(pageParam.getCurrent())
            .setRecords(items)
            .setSize(pageParam.getSize())
            .setTotal(result.getTotalCount());
        return pageResult;
    }

    /**
     * 删除素材
     */
    @SneakyThrows
    public void deleteFile(String mediaId) {
        WxMpMaterialService materialService = wxMpService.getMaterialService();
        materialService.materialDelete(mediaId);
    }

    /**
     * 上传 非图文素材
     * @see WxConsts.MediaFileType
     */
    @SneakyThrows
    public void uploadFile(String mediaType, MultipartFile multipartFile) {
        WxMpMaterialService materialService = wxMpService.getMaterialService();
        byte[] bytes = IoUtil.readBytes(multipartFile.getInputStream());
        String originalFilename = multipartFile.getOriginalFilename();
        String fileName = FileNameUtil.mainName(originalFilename);
        String fileType = FileTypeUtil.getType(new ByteArrayInputStream(bytes), originalFilename);
        File tempFile = FileUtil.createTempFile(new ByteArrayInputStream(bytes), fileName, fileType);
        WxMpMaterial material = new WxMpMaterial();
        material.setFile(tempFile);
        if (Objects.equals(mediaType, WxConsts.MediaFileType.VIDEO)) {
            material.setVideoTitle(fileName);
            material.setVideoIntroduction(fileName);
        }
        materialService.materialFileUpload(mediaType, material);
    }

}
