package org.dromara.daxpay.channel.wechat.service.isv;

import org.dromara.daxpay.channel.wechat.convert.isv.WechatIsvAppAuthConfigConvert;
import org.dromara.daxpay.channel.wechat.dao.isv.WechatIsvAppManager;
import org.dromara.daxpay.channel.wechat.dao.isv.WechatIsvAppAuthConfigManager;
import org.dromara.daxpay.channel.wechat.entity.isv.WechatIsvApp;
import org.dromara.daxpay.channel.wechat.entity.isv.WechatIsvAppAuthConfig;
import org.dromara.daxpay.channel.wechat.code.WechatIsvAppTypeEnum;
import org.dromara.daxpay.channel.wechat.param.isv.WechatIsvAppAuthConfigParam;
import org.dromara.daxpay.channel.wechat.result.isv.WechatIsvAppAuthConfigResult;
import org.dromara.daxpay.platform.core.code.CommonErrorCode;
import org.dromara.daxpay.platform.core.exception.BizInfoException;
import org.dromara.daxpay.platform.core.exception.DataNotExistException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/// # 微信服务商应用授权认证配置
///
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatIsvAppAuthConfigService {

    private final WechatIsvAppAuthConfigManager wechatIsvAppAuthConfigManager;
    private final WechatIsvAppManager wechatIsvAppManager;

    /// 根据应用ID查询授权认证配置
    public WechatIsvAppAuthConfigResult findByAppId(Long appId) {
        WechatIsvApp app = wechatIsvAppManager.findById(appId)
                .orElseThrow(() -> new DataNotExistException("error.channel.wechat.appNotFound"));
        return wechatIsvAppAuthConfigManager.findByAppId(appId)
                .map(config -> WechatIsvAppAuthConfigConvert.CONVERT.toResult(config)
                        .setAppSecretConfigured(StrUtil.isNotBlank(config.getAppSecret())))
                .orElseGet(() -> new WechatIsvAppAuthConfigResult()
                        .setAppId(appId)
                        .setAppSecretConfigured(false));
    }

    /// 保存应用授权认证配置
    @Transactional(rollbackFor = Exception.class)
    public void save(WechatIsvAppAuthConfigParam param) {
        WechatIsvApp app = wechatIsvAppManager.findById(param.getAppId())
                .orElseThrow(() -> new DataNotExistException("error.channel.wechat.appNotFound"));
        String authCallbackUrl = this.resolveAuthCallbackUrl(app, param.getAuthCallbackUrl());
        Optional<WechatIsvAppAuthConfig> existing = wechatIsvAppAuthConfigManager.findByAppId(param.getAppId());
        if (existing.isPresent()) {
            WechatIsvAppAuthConfig config = existing.get();
            WechatIsvAppAuthConfigConvert.CONVERT.copy(param, config);
            if (StrUtil.isBlank(config.getAppSecret())) {
                // 所有应用类型均须配置 AppSecret
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.wechat.appSecretRequired");
            }
            config.setAuthCallbackUrl(authCallbackUrl);
            wechatIsvAppAuthConfigManager.updateById(config);
        } else {
            if (StrUtil.isBlank(param.getAppSecret())) {
                // 所有应用类型首次保存均须填写 AppSecret
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.wechat.appSecretRequired");
            }
            WechatIsvAppAuthConfig config = WechatIsvAppAuthConfigConvert.CONVERT.toEntity(param);
            config.setAppId(param.getAppId());
            config.setAuthCallbackUrl(authCallbackUrl);
            wechatIsvAppAuthConfigManager.save(config);
        }
    }

    /// 仅公众号保留授权回调地址，其他类型清空
    private String resolveAuthCallbackUrl(WechatIsvApp app, String authCallbackUrl) {
        if (!WechatIsvAppTypeEnum.OFFICIAL_ACCOUNT.getCode().equals(app.getAppType())) {
            return null;
        }
        if (StrUtil.isBlank(authCallbackUrl)) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "validation.field.authCallbackUrl.notBlank");
        }
        return authCallbackUrl.trim();
    }

    /// 删除应用授权认证配置
    public void deleteByAppId(Long appId) {
        wechatIsvAppAuthConfigManager.deleteByAppId(appId);
    }
}
