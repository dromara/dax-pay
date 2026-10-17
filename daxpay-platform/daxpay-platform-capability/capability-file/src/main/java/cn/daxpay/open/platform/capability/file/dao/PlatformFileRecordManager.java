package cn.daxpay.open.platform.capability.file.dao;

import cn.daxpay.open.platform.capability.file.entity.PlatformFileRecord;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.hutool.core.util.NumberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 平台文件记录Manager
///
@Repository
@RequiredArgsConstructor
public class PlatformFileRecordManager extends BaseManager<PlatformFileRecordMapper, PlatformFileRecord> {

    /// 根据ID查询未删除的记录
    public Optional<PlatformFileRecord> findByIdNotDeleted(Long id) {
        return lambdaQuery()
                .eq(PlatformFileRecord::getId, id)
                .oneOpt();
    }

    /// 根据文件名查询文件记录
    public Optional<PlatformFileRecord> findByFilename(String filename) {
        return lambdaQuery()
                .eq(PlatformFileRecord::getFilename, filename)
                .oneOpt();
    }

    /// 按平台媒体ID或文件名查询文件记录
    ///
    /// 前端上传组件(BUploadImage)在 v-model 里持有的图片标识是文件名(uuid.ext), 而历史链路可能传纯数字文件ID;
    /// 本方法对两种形态统一解析: 纯数字按文件ID查, 其余按文件名查。
    ///
    /// @param mediaId 平台媒体ID(纯数字)或文件名(uuid.ext)
    public Optional<PlatformFileRecord> findByMediaIdOrFilename(String mediaId) {
        if (NumberUtil.isLong(mediaId)) {
            return findByIdNotDeleted(Long.parseLong(mediaId));
        }
        return findByFilename(mediaId);
    }
}
