package cn.daxpay.open.payment.douyin.dao.merchant;

import cn.daxpay.open.payment.douyin.entity.merchant.DyMchAppAuthConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 商户抖音应用授权认证配置
///
@Slf4j
@Repository
@RequiredArgsConstructor
public class DyMchAppAuthConfigManager extends BaseManager<DyMchAppAuthConfigMapper, DyMchAppAuthConfig> {

    /// 根据商户应用ID查询授权认证配置
    public Optional<DyMchAppAuthConfig> findByDyMchAppId(Long dyMchAppId) {
        return lambdaQuery()
                .eq(DyMchAppAuthConfig::getDyMchAppId, dyMchAppId)
                .oneOpt();
    }

    /// 根据商户应用ID删除授权认证配置
    public void deleteByDyMchAppId(Long dyMchAppId) {
        lambdaUpdate()
                .eq(DyMchAppAuthConfig::getDyMchAppId, dyMchAppId)
                .remove();
    }
}
