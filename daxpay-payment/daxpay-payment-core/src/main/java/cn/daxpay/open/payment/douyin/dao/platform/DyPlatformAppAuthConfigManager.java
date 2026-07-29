package cn.daxpay.open.payment.douyin.dao.platform;

import cn.daxpay.open.payment.douyin.entity.platform.DyPlatformAppAuthConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 平台抖音应用授权认证配置
///
@Slf4j
@Repository
@RequiredArgsConstructor
public class DyPlatformAppAuthConfigManager extends BaseManager<DyPlatformAppAuthConfigMapper, DyPlatformAppAuthConfig> {

    /// 根据平台应用ID查询授权认证配置
    public Optional<DyPlatformAppAuthConfig> findByDyPlatformAppId(Long dyPlatformAppId) {
        return lambdaQuery()
                .eq(DyPlatformAppAuthConfig::getDyPlatformAppId, dyPlatformAppId)
                .oneOpt();
    }

    /// 根据平台应用ID删除授权认证配置
    public void deleteByDyPlatformAppId(Long dyPlatformAppId) {
        lambdaUpdate()
                .eq(DyPlatformAppAuthConfig::getDyPlatformAppId, dyPlatformAppId)
                .remove();
    }
}
