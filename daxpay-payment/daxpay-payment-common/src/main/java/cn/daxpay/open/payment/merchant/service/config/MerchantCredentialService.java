package cn.daxpay.open.payment.merchant.service.config;

import cn.daxpay.open.platform.core.annotation.IgnoreTenant;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.common.config.properties.PlatformConfigProperties;
import cn.daxpay.open.platform.core.util.RsaSignUtil;
import cn.daxpay.open.payment.merchant.convert.config.MerchantCredentialConvert;
import cn.daxpay.open.payment.merchant.dao.config.MerchantCredentialManager;
import cn.daxpay.open.payment.merchant.dao.info.MerchantInfoManager;
import cn.daxpay.open.payment.merchant.entity.config.MerchantCredential;
import cn.daxpay.open.payment.merchant.param.config.MerchantCredentialParam;
import cn.daxpay.open.payment.merchant.result.config.MerchantCredentialResult;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 商户API配置服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantCredentialService {
    private final MerchantCredentialManager credentialManager;
    private final MerchantInfoManager merchantInfoManager;
    private final PlatformConfigProperties platformConfigProperties;

    /// 根据商户号查询
    @IgnoreTenant
    public MerchantCredentialResult findByMchNo(String mchNo) {
        String publicKey = platformConfigProperties.getKeyConfig().getPublicKey();
        var credentialOptional = credentialManager.findByMchNo(mchNo);
        if (credentialOptional.isEmpty()){
            var merchant = merchantInfoManager.findByMchNo(mchNo)
                    // 商户: 商户不存在
                    .orElseThrow(() -> new DataNotExistException("error.payment.merchant.merchantNotExist"));
            var credential = new MerchantCredential();
            credential.setMchNo(mchNo);
            credentialManager.save(credential);
            return credential.toResult().setPlatformPublicKey(publicKey);
        }
        return credentialOptional.get().toResult().setPlatformPublicKey(publicKey);
    }

    /// 更新
    public void update(MerchantCredentialParam param) {
        var credential = credentialManager.findByMchNo(param.getMchNo())
                // 商户: API配置不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.merchant.apiConfigNotExist"));
        // 判断公钥是否合法
        if (StrUtil.isNotBlank(param.getPublicKey())){
            RsaSignUtil.loadPublicKeyFromPem(param.getPublicKey());
        }
        MerchantCredentialConvert.CONVERT.copy(param, credential);
        credentialManager.updateById(credential);
    }
}
