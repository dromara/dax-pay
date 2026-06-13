package org.dromara.daxpay.channel.alipay.service.config;

import org.dromara.daxpay.channel.alipay.code.AlipayCode;
import org.dromara.daxpay.channel.alipay.convert.AlipayIsvAppKeyConfigConvert;
import org.dromara.daxpay.channel.alipay.dao.app.AlipayIsvAppManager;
import org.dromara.daxpay.channel.alipay.dao.config.AlipayIsvAppKeyConfigManager;
import org.dromara.daxpay.channel.alipay.entity.config.AlipayIsvAppKeyConfig;
import org.dromara.daxpay.channel.alipay.param.config.AlipayIsvAppKeyConfigParam;
import org.dromara.daxpay.channel.alipay.result.config.AlipayIsvAppKeyConfigResult;
import org.dromara.daxpay.platform.core.code.CommonErrorCode;
import org.dromara.daxpay.platform.core.exception.BizInfoException;
import org.dromara.daxpay.platform.core.exception.DataNotExistException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/// # 支付宝服务商应用密钥配置
///
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayIsvAppKeyConfigService {

    private final AlipayIsvAppKeyConfigManager alipayIsvAppKeyConfigManager;
    private final AlipayIsvAppManager alipayIsvAppManager;

    /// 根据应用ID查询密钥配置
    public AlipayIsvAppKeyConfigResult findByAppId(Long appId) {
        alipayIsvAppManager.findById(appId)
                .orElseThrow(() -> new DataNotExistException("error.channel.alipay.appNotFound"));
        return alipayIsvAppKeyConfigManager.findByAppId(appId)
                .map(AlipayIsvAppKeyConfigConvert.CONVERT::toResult)
                .orElseGet(() -> new AlipayIsvAppKeyConfigResult()
                        .setAppId(appId)
                        .setAuthType(AlipayCode.AuthType.AUTH_TYPE_KEY));
    }

    /// 保存应用密钥配置
    public void save(AlipayIsvAppKeyConfigParam param) {
        alipayIsvAppManager.findById(param.getAppId())
                .orElseThrow(() -> new DataNotExistException("error.channel.alipay.appNotFound"));
        Optional<AlipayIsvAppKeyConfig> existing = alipayIsvAppKeyConfigManager.findByAppId(param.getAppId());
        if (existing.isPresent()) {
            AlipayIsvAppKeyConfig config = existing.get();
            config.setAuthType(param.getAuthType());
            this.mergeSensitiveFields(config, param);
            alipayIsvAppKeyConfigManager.updateById(config);
            return;
        }
        this.validateForCreate(param);
        AlipayIsvAppKeyConfig config = AlipayIsvAppKeyConfigConvert.CONVERT.toEntity(param);
        config.setAppId(param.getAppId());
        alipayIsvAppKeyConfigManager.save(config);
    }

    /// 删除应用密钥配置
    public void deleteByAppId(Long appId) {
        alipayIsvAppKeyConfigManager.deleteByAppId(appId);
    }

    /// 合并敏感字段，空值表示不更新
    private void mergeSensitiveFields(AlipayIsvAppKeyConfig config, AlipayIsvAppKeyConfigParam param) {
        if (StrUtil.isNotBlank(param.getAlipayPublicKey())) {
            config.setAlipayPublicKey(param.getAlipayPublicKey());
        }
        if (StrUtil.isNotBlank(param.getPrivateKey())) {
            config.setPrivateKey(param.getPrivateKey());
        }
        if (StrUtil.isNotBlank(param.getAppCert())) {
            config.setAppCert(param.getAppCert());
        }
        if (StrUtil.isNotBlank(param.getAlipayCert())) {
            config.setAlipayCert(param.getAlipayCert());
        }
        if (StrUtil.isNotBlank(param.getAlipayRootCert())) {
            config.setAlipayRootCert(param.getAlipayRootCert());
        }
        if (StrUtil.isNotBlank(param.getSecretKey())) {
            config.setSecretKey(param.getSecretKey());
        }
    }

    /// 新增时的条件校验
    private void validateForCreate(AlipayIsvAppKeyConfigParam param) {
        if (StrUtil.isBlank(param.getPrivateKey())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.alipay.privateKeyRequired");
        }
        if (AlipayCode.AuthType.AUTH_TYPE_KEY.equals(param.getAuthType())) {
            if (StrUtil.isBlank(param.getAlipayPublicKey())) {
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.alipay.alipayPublicKeyRequired");
            }
        } else if (AlipayCode.AuthType.AUTH_TYPE_CART.equals(param.getAuthType())) {
            if (StrUtil.isBlank(param.getAppCert())) {
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.alipay.appCertRequired");
            }
            if (StrUtil.isBlank(param.getAlipayCert())) {
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.alipay.alipayCertRequired");
            }
            if (StrUtil.isBlank(param.getAlipayRootCert())) {
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.alipay.alipayRootCertRequired");
            }
        }
    }
}
