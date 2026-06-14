package org.dromara.daxpay.channel.alipay.service.direct;

import org.dromara.daxpay.channel.alipay.code.AlipayCode;
import org.dromara.daxpay.channel.alipay.convert.direct.AlipayDirectAppAuthConfigConvert;
import org.dromara.daxpay.channel.alipay.dao.direct.AlipayDirectAppManager;
import org.dromara.daxpay.channel.alipay.dao.direct.AlipayDirectAppAuthConfigManager;
import org.dromara.daxpay.channel.alipay.entity.direct.AlipayDirectApp;
import org.dromara.daxpay.channel.alipay.entity.direct.AlipayDirectAppAuthConfig;
import org.dromara.daxpay.channel.alipay.param.direct.AlipayDirectAppAuthConfigParam;
import org.dromara.daxpay.channel.alipay.result.direct.AlipayDirectAppAuthConfigResult;
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
/// 管理直连商户应用的用户授权认证配置，校验应用归属关系后执行新增或更新，校验用户标识类型的合法性。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayDirectAppAuthConfigService {

    private static final Set<String> USER_ID_TYPES = Set.of(
            AlipayCode.UserIdType.OPENID,
            AlipayCode.UserIdType.USERID,
            AlipayCode.UserIdType.OPENID_USERID
    );

    private final AlipayDirectAppAuthConfigManager alipayDirectAppAuthConfigManager;
    private final AlipayDirectAppManager alipayDirectAppManager;

    /// 根据应用ID查询授权认证配置
    public AlipayDirectAppAuthConfigResult findByAppId(Long appId) {
        AlipayDirectApp app = alipayDirectAppManager.findById(appId)
                .orElseThrow(() -> new DataNotExistException("error.channel.alipay.appNotFound"));
        return alipayDirectAppAuthConfigManager.findByAppId(appId)
                .map(AlipayDirectAppAuthConfigConvert.CONVERT::toResult)
                .orElseGet(() -> new AlipayDirectAppAuthConfigResult()
                        .setAppId(appId)
                        .setMchNo(app.getMchNo())
                        .setChannelMchNo(app.getChannelMchNo())
                        .setUserIdType(AlipayCode.UserIdType.OPENID));
    }

    /// 保存应用授权认证配置
    public void save(AlipayDirectAppAuthConfigParam param) {
        AlipayDirectApp app = alipayDirectAppManager.findById(param.getAppId())
                .orElseThrow(() -> new DataNotExistException("error.channel.alipay.appNotFound"));
        if (!app.getMchNo().equals(param.getMchNo()) || !app.getChannelMchNo().equals(param.getChannelMchNo())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.alipay.appNotFound");
        }
        this.validateUserIdType(param.getUserIdType());
        Optional<AlipayDirectAppAuthConfig> existing = alipayDirectAppAuthConfigManager.findByAppId(param.getAppId());
        if (existing.isPresent()) {
            AlipayDirectAppAuthConfig config = existing.get();
            config.setUserIdType(param.getUserIdType());
            config.setAuthCallbackUrl(param.getAuthCallbackUrl());
            alipayDirectAppAuthConfigManager.updateById(config);
            return;
        }
        AlipayDirectAppAuthConfig config = AlipayDirectAppAuthConfigConvert.CONVERT.toEntity(param);
        config.setMchNo(app.getMchNo());
        config.setChannelMchNo(app.getChannelMchNo());
        config.setAppId(param.getAppId());
        alipayDirectAppAuthConfigManager.save(config);
    }

    /// 删除应用授权认证配置
    public void deleteByAppId(Long appId) {
        alipayDirectAppAuthConfigManager.deleteByAppId(appId);
    }

    /// 校验用户标识类型
    private void validateUserIdType(String userIdType) {
        if (!USER_ID_TYPES.contains(userIdType)) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.alipay.userIdTypeInvalid");
        }
    }
}
