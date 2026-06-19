package cn.daxpay.open.platform.capability.file.service;

import cn.daxpay.open.platform.capability.file.dao.PlatformFileRecordManager;
import cn.daxpay.open.platform.capability.file.entity.PlatformFileRecord;
import cn.daxpay.open.platform.capability.file.param.PlatformFileRecordPageParam;
import cn.daxpay.open.platform.capability.file.result.PlatformFileRecordResult;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 平台文件记录服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class PlatformFileRecordService {

    private final PlatformFileRecordManager platformFileRecordManager;

    /// 分页查询
    public PageResult<PlatformFileRecordResult> page(PlatformFileRecordPageParam param) {
        Page<PlatformFileRecord> mpPage = MpUtil.getMpPage(param);
        Page<PlatformFileRecord> page = platformFileRecordManager.lambdaQuery()
                .like(StrUtil.isNotBlank(param.getFilename()), PlatformFileRecord::getFilename, param.getFilename())
                .like(StrUtil.isNotBlank(param.getOriginalFilename()), PlatformFileRecord::getOriginalFilename, param.getOriginalFilename())
                .eq(StrUtil.isNotBlank(param.getExt()), PlatformFileRecord::getExt, param.getExt())
                .eq(StrUtil.isNotBlank(param.getAccessType()), PlatformFileRecord::getAccessType, param.getAccessType())
                .eq(StrUtil.isNotBlank(param.getBizType()), PlatformFileRecord::getBizType, param.getBizType())
                .orderByDesc(PlatformFileRecord::getCreateTime)
                .page(mpPage);
        return MpUtil.toPageResult(page);
    }

    /// 查询详情
    public PlatformFileRecordResult findById(Long id) {
        return platformFileRecordManager.findByIdNotDeleted(id)
                // 文件: 文件记录不存在
                .orElseThrow(() -> new DataNotExistException("error.file.platformRecordNotExist"))
                .toResult();
    }
}
