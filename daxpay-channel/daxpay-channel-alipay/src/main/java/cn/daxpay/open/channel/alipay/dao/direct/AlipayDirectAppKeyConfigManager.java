package cn.daxpay.open.channel.alipay.dao.direct;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.channel.alipay.entity.direct.AlipayDirectAppKeyConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/// # 支付宝直连商户应用密钥配置
///
/// 直连商户应用密钥配置数据访问管理器，提供按应用ID查询和删除密钥配置的方法。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayDirectAppKeyConfigManager extends BaseManager<AlipayDirectAppKeyConfigMapper, AlipayDirectAppKeyConfig> {

    /// 根据应用ID查询密钥配置
    public Optional<AlipayDirectAppKeyConfig> findByAlipayDirectAppId(Long alipayDirectAppId) {
        return lambdaQuery()
                .eq(AlipayDirectAppKeyConfig::getAlipayDirectAppId, alipayDirectAppId)
                .oneOpt();
    }

    /// 根据应用ID删除密钥配置
    public void deleteByAlipayDirectAppId(Long alipayDirectAppId) {
        lambdaUpdate()
                .eq(AlipayDirectAppKeyConfig::getAlipayDirectAppId, alipayDirectAppId)
                .remove();
    }
}
