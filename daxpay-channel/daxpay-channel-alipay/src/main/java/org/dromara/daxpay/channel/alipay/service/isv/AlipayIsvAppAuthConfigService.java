package org.dromara.daxpay.channel.alipay.service.isv;

import org.dromara.daxpay.channel.alipay.code.AlipayCode;
import org.dromara.daxpay.channel.alipay.convert.isv.AlipayIsvAppAuthConfigConvert;
import org.dromara.daxpay.channel.alipay.dao.isv.AlipayIsvAppManager;
import org.dromara.daxpay.channel.alipay.dao.isv.AlipayIsvAppAuthConfigManager;
import org.dromara.daxpay.channel.alipay.entity.isv.AlipayIsvAppAuthConfig;
import org.dromara.daxpay.channel.alipay.param.isv.AlipayIsvAppAuthConfigParam;
import org.dromara.daxpay.channel.alipay.result.isv.AlipayIsvAppAuthConfigResult;
import org.dromara.daxpay.platform.core.code.CommonErrorCode;
import org.dromara.daxpay.platform.core.exception.BizInfoException;
import org.dromara.daxpay.platform.core.exception.DataNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

/// # 支付宝服务商应用授权认证配置
///
/// 管理服务商应用的用户授权认证配置，包括用户标识类型的合法性校验(仅支持openid/userid/openid_userid)。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayIsvAppAuthConfigService {

    private static final Set<String> USER_ID_TYPES = Set.of(
            AlipayCode.UserIdType.OPENID,
            AlipayCode.UserIdType.USERID,
            AlipayCode.UserIdType.OPENID_USERID
    );

    private final AlipayIsvAppAuthConfigManager alipayIsvAppAuthConfigManager;
    private final AlipayIsvAppManager alipayIsvAppManager;

    /// 根据应用ID查询授权认证配置
    public AlipayIsvAppAuthConfigResult findByAppId(Long appId) {
        alipayIsvAppManager.findById(appId)
                .orElseThrow(() -> new DataNotExistException("error.channel.alipay.appNotFound"));
        return alipayIsvAppAuthConfigManager.findByAppId(appId)
                .map(AlipayIsvAppAuthConfigConvert.CONVERT::toResult)
                .orElseGet(() -> new AlipayIsvAppAuthConfigResult()
                        .setAppId(appId)
                        .setUserIdType(AlipayCode.UserIdType.OPENID));
    }

    /// 保存应用授权认证配置
    public void save(AlipayIsvAppAuthConfigParam param) {
        alipayIsvAppManager.findById(param.getAppId())
                .orElseThrow(() -> new DataNotExistException("error.channel.alipay.appNotFound"));
        this.validateUserIdType(param.getUserIdType());
        Optional<AlipayIsvAppAuthConfig> existing = alipayIsvAppAuthConfigManager.findByAppId(param.getAppId());
        if (existing.isPresent()) {
            AlipayIsvAppAuthConfig config = existing.get();
            config.setUserIdType(param.getUserIdType());
            config.setAuthCallbackUrl(param.getAuthCallbackUrl());
            alipayIsvAppAuthConfigManager.updateById(config);
            return;
        }
        AlipayIsvAppAuthConfig config = AlipayIsvAppAuthConfigConvert.CONVERT.toEntity(param);
        config.setAppId(param.getAppId());
        alipayIsvAppAuthConfigManager.save(config);
    }

    /// 删除应用授权认证配置
    public void deleteByAppId(Long appId) {
        alipayIsvAppAuthConfigManager.deleteByAppId(appId);
    }

    /// 校验用户标识类型
    private void validateUserIdType(String userIdType) {
        if (!USER_ID_TYPES.contains(userIdType)) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.alipay.userIdTypeInvalid");
        }
    }
}
