package org.dromara.daxpay.channel.alipay.service.isv;

import org.dromara.daxpay.channel.alipay.code.AlipayCode;
import org.dromara.daxpay.channel.alipay.dao.isv.AlipayIsvAppManager;
import org.dromara.daxpay.channel.alipay.dao.isv.AlipayIsvAppAuthConfigManager;
import org.dromara.daxpay.channel.alipay.entity.isv.AlipayIsvAppAuthConfig;
import org.dromara.daxpay.channel.alipay.param.isv.AlipayIsvAppAuthConfigParam;
import org.dromara.daxpay.platform.core.code.CommonErrorCode;
import org.dromara.daxpay.platform.core.exception.BizInfoException;
import org.dromara.daxpay.platform.core.exception.DataNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

/// # 支付宝服务商应用授权认证配置
///
/// 管理服务商应用的用户授权认证配置，查询时不存在则创建默认记录，包括用户标识类型的合法性校验(仅支持openid/userid/openid_userid)。
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

    /// 根据应用ID查询授权认证配置, 不存在则创建默认记录
    @Transactional(rollbackFor = Exception.class)
    public AlipayIsvAppAuthConfig findByAlipayIsvAppId(Long alipayIsvAppId) {
        if (!alipayIsvAppManager.existedById(alipayIsvAppId)) {
            throw new DataNotExistException("error.channel.alipay.appNotFound");
        }
        var existing = alipayIsvAppAuthConfigManager.findByAlipayIsvAppId(alipayIsvAppId);
        if (existing.isPresent()) {
            return existing.get();
        }
        var config = new AlipayIsvAppAuthConfig()
                .setAlipayIsvAppId(alipayIsvAppId)
                .setUserIdType(AlipayCode.UserIdType.OPENID);
        alipayIsvAppAuthConfigManager.save(config);
        return config;
    }

    /// 保存应用授权认证配置(更新)
    @Transactional(rollbackFor = Exception.class)
    public void save(AlipayIsvAppAuthConfigParam param) {
        this.validateUserIdType(param.getUserIdType());
        var config = this.findByAlipayIsvAppId(param.getAlipayIsvAppId());
        config.setUserIdType(param.getUserIdType());
        config.setAuthCallbackUrl(param.getAuthCallbackUrl());
        alipayIsvAppAuthConfigManager.updateById(config);
    }

    /// 删除应用授权认证配置
    public void deleteByAlipayIsvAppId(Long alipayIsvAppId) {
        alipayIsvAppAuthConfigManager.deleteByAlipayIsvAppId(alipayIsvAppId);
    }

    /// 校验用户标识类型
    private void validateUserIdType(String userIdType) {
        if (!USER_ID_TYPES.contains(userIdType)) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.alipay.userIdTypeInvalid");
        }
    }
}
