package cn.daxpay.open.channel.alipay.service.direct;

import cn.daxpay.open.channel.alipay.code.AlipayCode;
import cn.daxpay.open.channel.alipay.convert.direct.AlipayDirectAppKeyConfigConvert;
import cn.daxpay.open.channel.alipay.dao.direct.AlipayDirectAppManager;
import cn.daxpay.open.channel.alipay.dao.direct.AlipayDirectAppKeyConfigManager;
import cn.daxpay.open.channel.alipay.entity.direct.AlipayDirectApp;
import cn.daxpay.open.channel.alipay.entity.direct.AlipayDirectAppKeyConfig;
import cn.daxpay.open.channel.alipay.param.direct.AlipayDirectAppKeyConfigParam;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/// # 支付宝直连商户应用密钥配置
///
/// 管理直连商户应用的密钥和证书配置，查询时不存在则创建默认记录，保存时合并敏感字段(空值表示不修改)。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayDirectAppKeyConfigService {

    private final AlipayDirectAppKeyConfigManager alipayDirectAppKeyConfigManager;
    private final AlipayDirectAppManager alipayDirectAppManager;

    /// 根据应用ID查询密钥配置, 不存在则创建默认记录
    @Transactional(rollbackFor = Exception.class)
    public AlipayDirectAppKeyConfig findByAlipayDirectAppId(Long alipayDirectAppId) {
        var app = alipayDirectAppManager.findById(alipayDirectAppId)
                // 支付宝: 直连商户应用不存在
                .orElseThrow(() -> new DataNotExistException("error.channel.alipay.mchAppNotFound"));
        var existing = alipayDirectAppKeyConfigManager.findByAlipayDirectAppId(alipayDirectAppId);
        if (existing.isPresent()) {
            return existing.get();
        }
        var config = new AlipayDirectAppKeyConfig()
                .setChannelMchNo(app.getChannelMchNo())
                .setAlipayDirectAppId(alipayDirectAppId)
                .setAuthType(AlipayCode.AuthType.AUTH_TYPE_KEY);
        config.setMchNo(app.getMchNo());
        alipayDirectAppKeyConfigManager.save(config);
        return config;
    }

    /// 保存应用密钥配置(更新)
    @Transactional(rollbackFor = Exception.class)
    public void save(AlipayDirectAppKeyConfigParam param) {
        var config = this.findByAlipayDirectAppId(param.getAlipayDirectAppId());
        AlipayDirectAppKeyConfigConvert.CONVERT.copy(param, config);
        alipayDirectAppKeyConfigManager.updateById(config);
    }

    /// 删除应用密钥配置
    public void deleteByAlipayDirectAppId(Long alipayDirectAppId) {
        alipayDirectAppKeyConfigManager.deleteByAlipayDirectAppId(alipayDirectAppId);
    }
}
