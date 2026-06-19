package cn.daxpay.open.channel.alipay.dao.isv;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.channel.alipay.entity.isv.AlipayIsvAppKeyConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/// # 支付宝服务商应用密钥配置
///
/// 服务商应用密钥配置数据访问管理器，提供按应用ID查询和删除密钥配置的方法。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayIsvAppKeyConfigManager extends BaseManager<AlipayIsvAppKeyConfigMapper, AlipayIsvAppKeyConfig> {

    /// 根据应用ID查询密钥配置
    public Optional<AlipayIsvAppKeyConfig> findByAlipayIsvAppId(Long alipayIsvAppId) {
        return lambdaQuery()
                .eq(AlipayIsvAppKeyConfig::getAlipayIsvAppId, alipayIsvAppId)
                .oneOpt();
    }

    /// 根据应用ID删除密钥配置
    public void deleteByAlipayIsvAppId(Long alipayIsvAppId) {
        lambdaUpdate()
                .eq(AlipayIsvAppKeyConfig::getAlipayIsvAppId, alipayIsvAppId)
                .remove();
    }
}
