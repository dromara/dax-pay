package cn.bootx.platform.starter.file.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.starter.file.entity.UploadFileInfo;
import cn.bootx.platform.starter.file.param.UploadFileQuery;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;

/**
 * @author xxm
 * @since 2022/1/12
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class UploadFileManager extends BaseManager<UploadFileMapper, UploadFileInfo> {


    /**
     * 根据URL查询
     */
    public Optional<UploadFileInfo> findByUrl(String url){
        return findByField(UploadFileInfo::getUrl, url);
    }

    /**
     * 根据URL删除
     */
    public boolean deleteByUrl(String url){
        return deleteByField(UploadFileInfo::getUrl, url);
    }

    /**
     * 分页
     */
    public Page<UploadFileInfo> page(PageParam pageParam, UploadFileQuery param) {
        Page<UploadFileInfo> mpPage = MpUtil.getMpPage(pageParam);
        return lambdaQuery()
                .like(StrUtil.isNotBlank(param.getOriginalFilename()), UploadFileInfo::getOriginalFilename, param.getOriginalFilename())
                .like(StrUtil.isNotBlank(param.getExt()), UploadFileInfo::getExt, param.getExt())
                .like(StrUtil.isNotBlank(param.getContentType()), UploadFileInfo::getContentType, param.getContentType())
                .ge(Objects.nonNull(param.getStartTime()), UploadFileInfo::getCreateTime, param.getStartTime())
                .le(Objects.nonNull(param.getEndTime()), UploadFileInfo::getCreateTime, param.getEndTime())
                .orderByDesc(UploadFileInfo::getId).page(mpPage);
    }

}
