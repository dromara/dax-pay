package cn.daxpay.open.platform.capability.file.dao;

import cn.daxpay.open.platform.capability.file.entity.PlatformFileRecord;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
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
}
