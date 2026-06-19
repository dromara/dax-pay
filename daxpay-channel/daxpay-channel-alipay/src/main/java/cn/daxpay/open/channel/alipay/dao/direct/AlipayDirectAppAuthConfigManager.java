package cn.daxpay.open.channel.alipay.dao.direct;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.channel.alipay.entity.direct.AlipayDirectAppAuthConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/// # 支付宝直连商户应用授权认证配置
///
/// 直连商户应用授权认证配置数据访问管理器，提供按应用ID查询和删除授权认证配置的方法。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayDirectAppAuthConfigManager extends BaseManager<AlipayDirectAppAuthConfigMapper, AlipayDirectAppAuthConfig> {

    /// 根据应用ID查询授权认证配置
    public Optional<AlipayDirectAppAuthConfig> findByAlipayDirectAppId(Long alipayDirectAppId) {
        return lambdaQuery()
                .eq(AlipayDirectAppAuthConfig::getAlipayDirectAppId, alipayDirectAppId)
                .oneOpt();
    }

    /// 根据应用ID删除授权认证配置
    public void deleteByAlipayDirectAppId(Long alipayDirectAppId) {
        lambdaUpdate()
                .eq(AlipayDirectAppAuthConfig::getAlipayDirectAppId, alipayDirectAppId)
                .remove();
    }
}
