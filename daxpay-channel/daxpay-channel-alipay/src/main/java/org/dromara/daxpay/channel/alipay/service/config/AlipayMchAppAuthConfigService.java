package org.dromara.daxpay.channel.alipay.service.config;

import org.dromara.daxpay.channel.alipay.code.AlipayCode;
import org.dromara.daxpay.channel.alipay.convert.AlipayMchAppAuthConfigConvert;
import org.dromara.daxpay.channel.alipay.dao.app.AlipayMchAppManager;
import org.dromara.daxpay.channel.alipay.dao.config.AlipayMchAppAuthConfigManager;
import org.dromara.daxpay.channel.alipay.entity.app.AlipayMchApp;
import org.dromara.daxpay.channel.alipay.entity.config.AlipayMchAppAuthConfig;
import org.dromara.daxpay.channel.alipay.param.config.AlipayMchAppAuthConfigParam;
import org.dromara.daxpay.channel.alipay.result.config.AlipayMchAppAuthConfigResult;
import org.dromara.daxpay.platform.core.code.CommonErrorCode;
import org.dromara.daxpay.platform.core.exception.BizInfoException;
import org.dromara.daxpay.platform.core.exception.DataNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

/// # 支付宝直连商户应用授权认证配置
///
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayMchAppAuthConfigService {

    private static final Set<String> USER_ID_TYPES = Set.of(
            AlipayCode.UserIdType.OPENID,
            AlipayCode.UserIdType.USERID,
            AlipayCode.UserIdType.OPENID_USERID
    );

    private final AlipayMchAppAuthConfigManager alipayMchAppAuthConfigManager;
    private final AlipayMchAppManager alipayMchAppManager;

    /// 根据应用ID查询授权认证配置
    public AlipayMchAppAuthConfigResult findByAppId(Long appId) {
        AlipayMchApp app = alipayMchAppManager.findById(appId)
                .orElseThrow(() -> new DataNotExistException("error.channel.alipay.appNotFound"));
        return alipayMchAppAuthConfigManager.findByAppId(appId)
                .map(AlipayMchAppAuthConfigConvert.CONVERT::toResult)
                .orElseGet(() -> new AlipayMchAppAuthConfigResult()
                        .setAppId(appId)
                        .setMchNo(app.getMchNo())
                        .setChannelMchNo(app.getChannelMchNo())
                        .setUserIdType(AlipayCode.UserIdType.OPENID));
    }

    /// 保存应用授权认证配置
    public void save(AlipayMchAppAuthConfigParam param) {
        AlipayMchApp app = alipayMchAppManager.findById(param.getAppId())
                .orElseThrow(() -> new DataNotExistException("error.channel.alipay.appNotFound"));
        if (!app.getMchNo().equals(param.getMchNo()) || !app.getChannelMchNo().equals(param.getChannelMchNo())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.alipay.appNotFound");
        }
        this.validateUserIdType(param.getUserIdType());
        Optional<AlipayMchAppAuthConfig> existing = alipayMchAppAuthConfigManager.findByAppId(param.getAppId());
        if (existing.isPresent()) {
            AlipayMchAppAuthConfig config = existing.get();
            config.setUserIdType(param.getUserIdType());
            config.setAuthCallbackUrl(param.getAuthCallbackUrl());
            alipayMchAppAuthConfigManager.updateById(config);
            return;
        }
        AlipayMchAppAuthConfig config = AlipayMchAppAuthConfigConvert.CONVERT.toEntity(param);
        config.setMchNo(app.getMchNo());
        config.setChannelMchNo(app.getChannelMchNo());
        config.setAppId(param.getAppId());
        alipayMchAppAuthConfigManager.save(config);
    }

    /// 删除应用授权认证配置
    public void deleteByAppId(Long appId) {
        alipayMchAppAuthConfigManager.deleteByAppId(appId);
    }

    /// 校验用户标识类型
    private void validateUserIdType(String userIdType) {
        if (!USER_ID_TYPES.contains(userIdType)) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.alipay.userIdTypeInvalid");
        }
    }
}
