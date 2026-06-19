package cn.daxpay.open.channel.alipay.dao.isv;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.channel.alipay.entity.isv.AlipayIsvAppAuthConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/// # 支付宝服务商应用授权认证配置
///
/// 服务商应用授权认证配置数据访问管理器，提供按应用ID查询和删除授权认证配置的方法。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayIsvAppAuthConfigManager extends BaseManager<AlipayIsvAppAuthConfigMapper, AlipayIsvAppAuthConfig> {

    /// 根据应用ID查询授权认证配置
    public Optional<AlipayIsvAppAuthConfig> findByAlipayIsvAppId(Long alipayIsvAppId) {
        return lambdaQuery()
                .eq(AlipayIsvAppAuthConfig::getAlipayIsvAppId, alipayIsvAppId)
                .oneOpt();
    }

    /// 根据应用ID删除授权认证配置
    public void deleteByAlipayIsvAppId(Long alipayIsvAppId) {
        lambdaUpdate()
                .eq(AlipayIsvAppAuthConfig::getAlipayIsvAppId, alipayIsvAppId)
                .remove();
    }
}
