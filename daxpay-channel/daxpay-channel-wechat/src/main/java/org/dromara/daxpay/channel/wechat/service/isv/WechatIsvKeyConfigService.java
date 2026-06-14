package org.dromara.daxpay.channel.wechat.service.isv;

import org.dromara.daxpay.channel.wechat.convert.isv.WechatIsvKeyConfigConvert;
import org.dromara.daxpay.channel.wechat.dao.isv.WechatIsvKeyConfigManager;
import org.dromara.daxpay.channel.wechat.entity.isv.WechatIsvKeyConfig;
import org.dromara.daxpay.channel.wechat.param.isv.WechatIsvKeyConfigParam;
import org.dromara.daxpay.channel.wechat.result.isv.WechatIsvKeyConfigResult;
import org.dromara.daxpay.platform.core.code.CommonErrorCode;
import org.dromara.daxpay.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/// # 微信服务商密钥配置
///
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatIsvKeyConfigService {

    private final WechatIsvKeyConfigManager wechatIsvKeyConfigManager;

    /// 根据产品编码查询微信服务商密钥配置（平台为唯一服务商，密钥全局唯一）
    /// 注意: 微信服务商模式不支持沙箱环境
    public WechatIsvKeyConfigResult findByProduct(String product) {
        Optional<WechatIsvKeyConfig> config = wechatIsvKeyConfigManager.findByProduct(product);
        return config.map(WechatIsvKeyConfigConvert.CONVERT::toResult).orElse(null);
    }

    /// 保存微信服务商密钥配置
    /// 注意: 微信服务商模式不支持沙箱环境
    @Transactional(rollbackFor = Exception.class)
    public void saveConfig(WechatIsvKeyConfigParam param) {
        Optional<WechatIsvKeyConfig> existing = wechatIsvKeyConfigManager.findByProduct(param.getProduct());
        WechatIsvKeyConfig config;
        if (existing.isPresent()) {
            config = existing.get();
            config.setWxMchId(param.getWxMchId());
            WechatIsvKeyConfigConvert.CONVERT.copy(param, config);
            wechatIsvKeyConfigManager.updateById(config);
        } else {
            this.validateForCreate(param);
            config = WechatIsvKeyConfigConvert.CONVERT.toEntity(param);
            config.setProduct(param.getProduct());
            wechatIsvKeyConfigManager.save(config);
        }
    }

    /// 新增时的必填校验
    private void validateForCreate(WechatIsvKeyConfigParam param) {
        if (StrUtil.isBlank(param.getPrivateKey())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.wechat.privateKeyRequired");
        }
        if (StrUtil.isBlank(param.getApiKeyV3())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.wechat.apiV3KeyRequired");
        }
        if (StrUtil.isBlank(param.getPublicKey())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.wechat.publicKeyRequired");
        }
        if (StrUtil.isBlank(param.getPrivateCert())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.wechat.privateCertRequired");
        }
    }
}
