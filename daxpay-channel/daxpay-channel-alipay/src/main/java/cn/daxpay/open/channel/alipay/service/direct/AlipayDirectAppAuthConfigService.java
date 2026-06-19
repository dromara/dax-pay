package cn.daxpay.open.channel.alipay.service.direct;

import cn.daxpay.open.channel.alipay.code.AlipayCode;
import cn.daxpay.open.channel.alipay.dao.direct.AlipayDirectAppManager;
import cn.daxpay.open.channel.alipay.dao.direct.AlipayDirectAppAuthConfigManager;
import cn.daxpay.open.channel.alipay.entity.direct.AlipayDirectApp;
import cn.daxpay.open.channel.alipay.entity.direct.AlipayDirectAppAuthConfig;
import cn.daxpay.open.channel.alipay.param.direct.AlipayDirectAppAuthConfigParam;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

/// # 支付宝直连商户应用授权认证配置
///
/// 管理直连商户应用的用户授权认证配置，查询时不存在则创建默认记录，保存时校验应用归属关系，校验用户标识类型的合法性。
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

    /// 根据应用ID查询授权认证配置, 不存在则创建默认记录
    @Transactional(rollbackFor = Exception.class)
    public AlipayDirectAppAuthConfig findByAlipayDirectAppId(Long alipayDirectAppId) {
        var app = alipayDirectAppManager.findById(alipayDirectAppId)
                // 支付宝: 直连商户应用不存在
                .orElseThrow(() -> new DataNotExistException("error.channel.alipay.mchAppNotFound"));
        var existing = alipayDirectAppAuthConfigManager.findByAlipayDirectAppId(alipayDirectAppId);
        if (existing.isPresent()) {
            return existing.get();
        }
        var config = new AlipayDirectAppAuthConfig()
                .setChannelMchNo(app.getChannelMchNo())
                .setAlipayDirectAppId(alipayDirectAppId)
                .setUserIdType(AlipayCode.UserIdType.OPENID);
        config.setMchNo(app.getMchNo());
        alipayDirectAppAuthConfigManager.save(config);
        return config;
    }

    /// 保存应用授权认证配置(更新)
    @Transactional(rollbackFor = Exception.class)
    public void save(AlipayDirectAppAuthConfigParam param) {
        var app = alipayDirectAppManager.findById(param.getAlipayDirectAppId())
                // 支付宝: 直连商户应用不存在
                .orElseThrow(() -> new DataNotExistException("error.channel.alipay.mchAppNotFound"));
        if (!app.getMchNo().equals(param.getMchNo()) || !app.getChannelMchNo().equals(param.getChannelMchNo())) {
            // 支付宝: 直连商户应用不存在或商户号归属不匹配
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.alipay.mchAppNotFound");
        }
        this.validateUserIdType(param.getUserIdType());
        var config = this.findByAlipayDirectAppId(param.getAlipayDirectAppId());
        config.setUserIdType(param.getUserIdType());
        config.setAuthCallbackUrl(param.getAuthCallbackUrl());
        alipayDirectAppAuthConfigManager.updateById(config);
    }

    /// 删除应用授权认证配置
    public void deleteByAlipayDirectAppId(Long alipayDirectAppId) {
        alipayDirectAppAuthConfigManager.deleteByAlipayDirectAppId(alipayDirectAppId);
    }

    /// 校验用户标识类型
    private void validateUserIdType(String userIdType) {
        if (!USER_ID_TYPES.contains(userIdType)) {
            // 支付宝: 用户标识类型不合法
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.alipay.userIdTypeInvalid");
        }
    }
}
