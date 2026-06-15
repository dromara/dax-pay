package org.dromara.daxpay.channel.alipay.service.isv;

import org.dromara.daxpay.channel.alipay.code.AlipayCode;
import org.dromara.daxpay.channel.alipay.convert.isv.AlipayIsvAppKeyConfigConvert;
import org.dromara.daxpay.channel.alipay.dao.isv.AlipayIsvAppManager;
import org.dromara.daxpay.channel.alipay.dao.isv.AlipayIsvAppKeyConfigManager;
import org.dromara.daxpay.channel.alipay.entity.isv.AlipayIsvAppKeyConfig;
import org.dromara.daxpay.channel.alipay.param.isv.AlipayIsvAppKeyConfigParam;
import org.dromara.daxpay.platform.core.exception.DataNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/// # 支付宝服务商应用密钥配置
///
/// 管理服务商应用的密钥和证书配置，查询时不存在则创建默认记录，保存时合并敏感字段(空值表示不修改)。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayIsvAppKeyConfigService {

    private final AlipayIsvAppKeyConfigManager alipayIsvAppKeyConfigManager;
    private final AlipayIsvAppManager alipayIsvAppManager;

    /// 根据应用ID查询密钥配置, 不存在则创建默认记录
    @Transactional(rollbackFor = Exception.class)
    public AlipayIsvAppKeyConfig findByAlipayIsvAppId(Long alipayIsvAppId) {
        if (!alipayIsvAppManager.existedById(alipayIsvAppId)) {
            // 支付宝: 服务商应用不存在
            throw new DataNotExistException("error.channel.alipay.appNotFound");
        }
        var existing = alipayIsvAppKeyConfigManager.findByAlipayIsvAppId(alipayIsvAppId);
        if (existing.isPresent()) {
            return existing.get();
        }
        var config = new AlipayIsvAppKeyConfig()
                .setAlipayIsvAppId(alipayIsvAppId)
                .setAuthType(AlipayCode.AuthType.AUTH_TYPE_KEY);
        alipayIsvAppKeyConfigManager.save(config);
        return config;
    }

    /// 保存应用密钥配置(更新)
    @Transactional(rollbackFor = Exception.class)
    public void save(AlipayIsvAppKeyConfigParam param) {
        AlipayIsvAppKeyConfig config = this.findByAlipayIsvAppId(param.getAlipayIsvAppId());
        config.setAuthType(param.getAuthType());
        AlipayIsvAppKeyConfigConvert.CONVERT.copy(param, config);
        alipayIsvAppKeyConfigManager.updateById(config);
    }

    /// 删除应用密钥配置
    public void deleteByAlipayIsvAppId(Long alipayIsvAppId) {
        alipayIsvAppKeyConfigManager.deleteByAlipayIsvAppId(alipayIsvAppId);
    }
}
