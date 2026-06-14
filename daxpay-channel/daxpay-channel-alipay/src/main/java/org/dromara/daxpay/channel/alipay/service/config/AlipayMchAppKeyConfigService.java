package org.dromara.daxpay.channel.alipay.service.config;

import org.dromara.daxpay.channel.alipay.code.AlipayCode;
import org.dromara.daxpay.channel.alipay.convert.AlipayMchAppKeyConfigConvert;
import org.dromara.daxpay.channel.alipay.dao.app.AlipayMchAppManager;
import org.dromara.daxpay.channel.alipay.dao.config.AlipayMchAppKeyConfigManager;
import org.dromara.daxpay.channel.alipay.entity.app.AlipayMchApp;
import org.dromara.daxpay.channel.alipay.entity.config.AlipayMchAppKeyConfig;
import org.dromara.daxpay.channel.alipay.param.config.AlipayMchAppKeyConfigParam;
import org.dromara.daxpay.channel.alipay.result.config.AlipayMchAppKeyConfigResult;
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
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayMchAppKeyConfigService {

    private final AlipayMchAppKeyConfigManager alipayMchAppKeyConfigManager;
    private final AlipayMchAppManager alipayMchAppManager;

    /// 根据应用ID查询密钥配置
    public AlipayMchAppKeyConfigResult findByAppId(Long appId) {
        AlipayMchApp app = alipayMchAppManager.findById(appId)
                .orElseThrow(() -> new DataNotExistException("error.channel.alipay.appNotFound"));
        return alipayMchAppKeyConfigManager.findByAppId(appId)
                .map(AlipayMchAppKeyConfigConvert.CONVERT::toResult)
                .orElseGet(() -> new AlipayMchAppKeyConfigResult()
                        .setAppId(appId)
                        .setMchNo(app.getMchNo())
                        .setChannelMchNo(app.getChannelMchNo())
                        .setAuthType(AlipayCode.AuthType.AUTH_TYPE_KEY));
    }

    /// 保存应用密钥配置
    public void save(AlipayMchAppKeyConfigParam param) {
        AlipayMchApp app = alipayMchAppManager.findById(param.getAppId())
                .orElseThrow(() -> new DataNotExistException("error.channel.alipay.appNotFound"));
        if (!app.getMchNo().equals(param.getMchNo()) || !app.getChannelMchNo().equals(param.getChannelMchNo())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.alipay.appNotFound");
        }
        Optional<AlipayMchAppKeyConfig> existing = alipayMchAppKeyConfigManager.findByAppId(param.getAppId());
        if (existing.isPresent()) {
            AlipayMchAppKeyConfig config = existing.get();
            config.setAuthType(param.getAuthType());
            this.mergeSensitiveFields(config, param);
            alipayMchAppKeyConfigManager.updateById(config);
            return;
        }
        this.validateForCreate(param);
        AlipayMchAppKeyConfig config = AlipayMchAppKeyConfigConvert.CONVERT.toEntity(param);
        config.setMchNo(app.getMchNo());
        config.setChannelMchNo(app.getChannelMchNo());
        config.setAppId(param.getAppId());
        alipayMchAppKeyConfigManager.save(config);
    }

    /// 删除应用密钥配置
    public void deleteByAppId(Long appId) {
        alipayMchAppKeyConfigManager.deleteByAppId(appId);
    }

    /// 合并敏感字段，空值表示不更新
    private void mergeSensitiveFields(AlipayMchAppKeyConfig config, AlipayMchAppKeyConfigParam param) {
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
    private void validateForCreate(AlipayMchAppKeyConfigParam param) {
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
