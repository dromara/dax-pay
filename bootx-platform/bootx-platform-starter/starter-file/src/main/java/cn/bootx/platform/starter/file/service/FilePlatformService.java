package cn.bootx.platform.starter.file.service;

import cn.bootx.platform.common.mybatisplus.base.MpRealDelEntity;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.starter.auth.util.SecurityUtil;
import cn.bootx.platform.starter.file.dao.FilePlatformManager;
import cn.bootx.platform.starter.file.entity.FilePlatform;
import cn.bootx.platform.starter.file.param.FilePlatformParam;
import cn.bootx.platform.starter.file.result.FilePlatformResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 存储平台
 * @author xxm
 * @since 2024/8/12
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FilePlatformService {
    private final FilePlatformManager filePlatformManager;


    /**
     * 获取全部存储平台
     */
    public List<FilePlatformResult> findAll(){
        return MpUtil.toListResult(filePlatformManager.findAll());
    }

    /**
     * 获取存储平台
     */
    public FilePlatformResult findById(Long id){
        return filePlatformManager.findById(id).map(FilePlatform::toResult).orElseThrow(() -> new DataNotExistException("存储平台不存在"));
    }

    /**
     * 更新
     */
    public void updateUrl(FilePlatformParam filePlatform){
        FilePlatform platform = filePlatformManager.findById(filePlatform.getId())
                .orElseThrow(() -> new DataNotExistException("存储平台不存在"));
        platform.setUrl(filePlatform.getUrl());
        filePlatformManager.updateById(platform);
    }

    /**
     * 设为默认
     */
    @Transactional(rollbackFor = Exception.class)
    public void setDefault(Long id){
        filePlatformManager.lambdaUpdate()
                .set(FilePlatform::isDefaultPlatform, false)
                .eq(FilePlatform::isDefaultPlatform, true)
                .set(FilePlatform::getLastModifiedTime, LocalDateTime.now())
                .set(MpRealDelEntity::getLastModifier, SecurityUtil.getUserIdOrDefaultId())
                .update();
        filePlatformManager.lambdaUpdate()
                .eq(FilePlatform::getId, id)
                .set(FilePlatform::getLastModifiedTime, LocalDateTime.now())
                .set(MpRealDelEntity::getLastModifier, SecurityUtil.getUserIdOrDefaultId())
                .set(FilePlatform::isDefaultPlatform, true)
                .update();
    }
}
