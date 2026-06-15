package org.dromara.daxpay.channel.wechat.service.direct;

import org.dromara.daxpay.channel.wechat.convert.direct.WechatDirectAppAuthConfigConvert;
import org.dromara.daxpay.channel.wechat.dao.direct.WechatDirectAppManager;
import org.dromara.daxpay.channel.wechat.dao.direct.WechatDirectAppAuthConfigManager;
import org.dromara.daxpay.channel.wechat.entity.direct.WechatDirectApp;
import org.dromara.daxpay.channel.wechat.entity.direct.WechatDirectAppAuthConfig;
import org.dromara.daxpay.channel.wechat.param.direct.WechatDirectAppAuthConfigParam;
import org.dromara.daxpay.platform.core.code.CommonErrorCode;
import org.dromara.daxpay.platform.core.exception.BizInfoException;
import org.dromara.daxpay.platform.core.exception.DataNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 微信直连商户应用授权认证配置
///
/// 管理直连商户应用的授权认证配置，查询时不存在则创建默认记录，保存时校验应用归属关系。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatDirectAppAuthConfigService {

    private final WechatDirectAppAuthConfigManager wechatDirectAppAuthConfigManager;
    private final WechatDirectAppManager wechatDirectAppManager;

    /// 根据应用ID查询授权认证配置, 不存在则创建默认记录
    @Transactional(rollbackFor = Exception.class)
    public WechatDirectAppAuthConfig findByWechatDirectAppId(Long wechatDirectAppId) {
        var existing = wechatDirectAppAuthConfigManager.findByWechatDirectAppId(wechatDirectAppId);
        if (existing.isPresent()) {
            return existing.get();
        }
        var app = wechatDirectAppManager.findById(wechatDirectAppId)
                // 微信: 直连商户应用不存在
                .orElseThrow(() -> new DataNotExistException("error.channel.wechat.mchAppNotFound"));
        var config = new WechatDirectAppAuthConfig()
                .setChannelMchNo(app.getChannelMchNo())
                .setWechatDirectAppId(wechatDirectAppId);
        config.setMchNo(app.getMchNo());
        wechatDirectAppAuthConfigManager.save(config);
        return config;
    }

    /// 保存应用授权认证配置(更新)
    @Transactional(rollbackFor = Exception.class)
    public void save(WechatDirectAppAuthConfigParam param) {
        var app = wechatDirectAppManager.findById(param.getWechatDirectAppId())
                // 微信: 直连商户应用不存在
                .orElseThrow(() -> new DataNotExistException("error.channel.wechat.mchAppNotFound"));
        if (!app.getMchNo().equals(param.getMchNo()) || !app.getChannelMchNo().equals(param.getChannelMchNo())) {
            // 微信: 直连商户应用不存在或商户号归属不匹配
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.wechat.mchAppNotFound");
        }
        var config = this.findByWechatDirectAppId(param.getWechatDirectAppId());
        config.setAuthCallbackUrl(param.getAuthCallbackUrl());
        WechatDirectAppAuthConfigConvert.CONVERT.copy(param, config);
        wechatDirectAppAuthConfigManager.updateById(config);
    }

    /// 删除应用授权认证配置
    public void deleteByWechatDirectAppId(Long wechatDirectAppId) {
        wechatDirectAppAuthConfigManager.deleteByWechatDirectAppId(wechatDirectAppId);
    }
}
