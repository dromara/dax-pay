package cn.daxpay.open.payment.douyin.service.platform;

import cn.daxpay.open.payment.douyin.dao.platform.DyPlatformAppAuthConfigManager;
import cn.daxpay.open.payment.douyin.dao.platform.DyPlatformAppManager;
import cn.daxpay.open.payment.douyin.entity.platform.DyPlatformAppAuthConfig;
import cn.daxpay.open.payment.douyin.param.platform.DyPlatformAppAuthConfigParam;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 平台抖音应用授权认证配置
///
/// 管理平台应用的授权认证配置；查询时不存在则创建默认记录；空 secret 表示不更新。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class DyPlatformAppAuthConfigService {

    private final DyPlatformAppAuthConfigManager dyPlatformAppAuthConfigManager;
    private final DyPlatformAppManager dyPlatformAppManager;

    /// 根据平台应用ID查询授权认证配置，不存在则创建默认记录
    @Transactional(rollbackFor = Exception.class)
    public DyPlatformAppAuthConfig findByDyPlatformAppId(Long dyPlatformAppId) {
        if (!dyPlatformAppManager.existedById(dyPlatformAppId)) {
            // 抖音: 平台应用不存在
            throw new DataNotExistException("error.payment.douyin.appNotFound");
        }
        var existing = dyPlatformAppAuthConfigManager.findByDyPlatformAppId(dyPlatformAppId);
        if (existing.isPresent()) {
            return existing.get();
        }
        var config = new DyPlatformAppAuthConfig()
                .setDyPlatformAppId(dyPlatformAppId);
        dyPlatformAppAuthConfigManager.save(config);
        return config;
    }

    /// 保存应用授权认证配置（空 secret 表示不改）
    @Transactional(rollbackFor = Exception.class)
    public void save(DyPlatformAppAuthConfigParam param) {
        var config = this.findByDyPlatformAppId(param.getDyPlatformAppId());
        // 空 secret 表示不更新
        if (StrUtil.isNotBlank(param.getAppSecret())) {
            config.setAppSecret(param.getAppSecret());
        }
        dyPlatformAppAuthConfigManager.updateById(config);
    }

    /// 删除应用授权认证配置
    public void deleteByDyPlatformAppId(Long dyPlatformAppId) {
        dyPlatformAppAuthConfigManager.deleteByDyPlatformAppId(dyPlatformAppId);
    }
}
