package cn.bootx.platform.starter.file.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.starter.file.UploadFileParam;
import cn.bootx.platform.starter.file.entity.UploadFileInfo;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Objects;

/**
 * @author xxm
 * @since 2022/1/12
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class UploadFileManager extends BaseManager<UploadFileMapper, UploadFileInfo> {

    /**
     * 分页
     */
    public Page<UploadFileInfo> page(PageParam pageParam, UploadFileParam param) {
        Page<UploadFileInfo> mpPage = MpUtil.getMpPage(pageParam, UploadFileInfo.class);
        return lambdaQuery()
                .like(StrUtil.isNotBlank(param.getOriginalFilename()), UploadFileInfo::getOriginalFilename, param.getOriginalFilename())
                .like(StrUtil.isNotBlank(param.getExt()), UploadFileInfo::getExt, param.getExt())
                .like(StrUtil.isNotBlank(param.getContentType()), UploadFileInfo::getContentType, param.getContentType())
                .ge(Objects.nonNull(param.getStartTime()), UploadFileInfo::getCreateTime, param.getStartTime())
                .le(Objects.nonNull(param.getEndTime()), UploadFileInfo::getCreateTime, param.getEndTime())
                .orderByDesc(UploadFileInfo::getId).page(mpPage);
    }

}
