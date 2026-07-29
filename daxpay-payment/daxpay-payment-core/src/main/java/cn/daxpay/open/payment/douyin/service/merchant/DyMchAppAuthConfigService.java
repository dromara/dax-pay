package cn.daxpay.open.payment.douyin.service.merchant;

import cn.daxpay.open.payment.douyin.dao.merchant.DyMchAppAuthConfigManager;
import cn.daxpay.open.payment.douyin.dao.merchant.DyMchAppManager;
import cn.daxpay.open.payment.douyin.entity.merchant.DyMchAppAuthConfig;
import cn.daxpay.open.payment.douyin.param.merchant.DyMchAppAuthConfigParam;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 商户抖音应用授权认证配置
///
/// 管理商户应用的授权认证配置；查询时不存在则创建默认记录；空 secret 表示不更新。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class DyMchAppAuthConfigService {

    private final DyMchAppAuthConfigManager dyMchAppAuthConfigManager;
    private final DyMchAppManager dyMchAppManager;

    /// 根据商户应用ID查询授权认证配置，不存在则创建默认记录
    @Transactional(rollbackFor = Exception.class)
    public DyMchAppAuthConfig findByDyMchAppId(Long dyMchAppId) {
        var app = dyMchAppManager.findById(dyMchAppId)
                // 抖音: 商户应用不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.douyin.mchAppNotFound"));
        var existing = dyMchAppAuthConfigManager.findByDyMchAppId(dyMchAppId);
        if (existing.isPresent()) {
            return existing.get();
        }
        var config = new DyMchAppAuthConfig()
                .setDyMchAppId(dyMchAppId);
        // 与主表商户号一致
        config.setMchNo(app.getMchNo());
        dyMchAppAuthConfigManager.save(config);
        return config;
    }

    /// 保存应用授权认证配置（空 secret 表示不改）
    @Transactional(rollbackFor = Exception.class)
    public void save(DyMchAppAuthConfigParam param) {
        var config = this.findByDyMchAppId(param.getDyMchAppId());
        // 空 secret 表示不更新
        if (StrUtil.isNotBlank(param.getAppSecret())) {
            config.setAppSecret(param.getAppSecret());
        }
        dyMchAppAuthConfigManager.updateById(config);
    }

    /// 删除应用授权认证配置
    public void deleteByDyMchAppId(Long dyMchAppId) {
        dyMchAppAuthConfigManager.deleteByDyMchAppId(dyMchAppId);
    }
}
