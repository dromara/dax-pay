package org.dromara.daxpay.channel.alipay.service.direct;

import org.dromara.daxpay.channel.alipay.code.AlipayCode;
import org.dromara.daxpay.channel.alipay.convert.AlipayDirectAppKeyConfigConvert;
import org.dromara.daxpay.channel.alipay.dao.direct.AlipayDirectAppManager;
import org.dromara.daxpay.channel.alipay.dao.direct.AlipayDirectAppKeyConfigManager;
import org.dromara.daxpay.channel.alipay.entity.direct.AlipayDirectApp;
import org.dromara.daxpay.channel.alipay.entity.direct.AlipayDirectAppKeyConfig;
import org.dromara.daxpay.channel.alipay.param.direct.AlipayDirectAppKeyConfigParam;
import org.dromara.daxpay.channel.alipay.result.direct.AlipayDirectAppKeyConfigResult;
import org.dromara.daxpay.platform.core.code.CommonErrorCode;
import org.dromara.daxpay.platform.core.exception.BizInfoException;
import org.dromara.daxpay.platform.core.exception.DataNotExistException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/// # 支付宝直连商户应用密钥配置
///
/// 管理直连商户应用的密钥和证书配置，校验应用归属关系后执行新增或更新，更新时合并敏感字段。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayDirectAppKeyConfigService {

    private final AlipayDirectAppKeyConfigManager alipayDirectAppKeyConfigManager;
    private final AlipayDirectAppManager alipayDirectAppManager;

    /// 根据应用ID查询密钥配置
    public AlipayDirectAppKeyConfigResult findByAppId(Long appId) {
        AlipayDirectApp app = alipayDirectAppManager.findById(appId)
                .orElseThrow(() -> new DataNotExistException("error.channel.alipay.appNotFound"));
        return alipayDirectAppKeyConfigManager.findByAppId(appId)
                .map(AlipayDirectAppKeyConfigConvert.CONVERT::toResult)
                .orElseGet(() -> new AlipayDirectAppKeyConfigResult()
                        .setAppId(appId)
                        .setMchNo(app.getMchNo())
                        .setChannelMchNo(app.getChannelMchNo())
                        .setAuthType(AlipayCode.AuthType.AUTH_TYPE_KEY));
    }

    /// 保存应用密钥配置
    public void save(AlipayDirectAppKeyConfigParam param) {
        AlipayDirectApp app = alipayDirectAppManager.findById(param.getAppId())
                .orElseThrow(() -> new DataNotExistException("error.channel.alipay.appNotFound"));
        if (!app.getMchNo().equals(param.getMchNo()) || !app.getChannelMchNo().equals(param.getChannelMchNo())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.alipay.appNotFound");
        }
        Optional<AlipayDirectAppKeyConfig> existing = alipayDirectAppKeyConfigManager.findByAppId(param.getAppId());
        if (existing.isPresent()) {
            AlipayDirectAppKeyConfig config = existing.get();
            config.setAuthType(param.getAuthType());
            this.mergeSensitiveFields(config, param);
            alipayDirectAppKeyConfigManager.updateById(config);
            return;
        }
        this.validateForCreate(param);
        AlipayDirectAppKeyConfig config = AlipayDirectAppKeyConfigConvert.CONVERT.toEntity(param);
        config.setMchNo(app.getMchNo());
        config.setChannelMchNo(app.getChannelMchNo());
        config.setAppId(param.getAppId());
        alipayDirectAppKeyConfigManager.save(config);
    }

    /// 删除应用密钥配置
    public void deleteByAppId(Long appId) {
        alipayDirectAppKeyConfigManager.deleteByAppId(appId);
    }

    /// 合并敏感字段，空值表示不更新
    private void mergeSensitiveFields(AlipayDirectAppKeyConfig config, AlipayDirectAppKeyConfigParam param) {
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
    private void validateForCreate(AlipayDirectAppKeyConfigParam param) {
        if (StrUtil.isBlank(param.getPrivateKey())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.alipay.privateKeyRequired");
        }
        if (AlipayCode.AuthType.AUTH_TYPE_KEY.equals(param.getAuthType())) {
            if (StrUtil.isBlank(param.getAlipayPublicKey())) {
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.alipay.alipayPublicKeyRequired");
            }
        } else if (AlipayCode.AuthType.AUTH_TYPE_CERT.equals(param.getAuthType())) {
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
