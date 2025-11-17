package org.dromara.daxpay.payment.merchant.service.config;

import cn.bootx.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.payment.common.properties.DaxPayProperties;
import org.dromara.daxpay.payment.common.util.RsaSignUtil;
import org.dromara.daxpay.payment.merchant.convert.config.MerchantCredentialConvert;
import org.dromara.daxpay.payment.merchant.dao.config.MerchantCredentialManager;
import org.dromara.daxpay.payment.merchant.dao.info.MerchantManager;
import org.dromara.daxpay.payment.merchant.entity.config.MerchantCredential;
import org.dromara.daxpay.payment.merchant.param.config.MerchantCredentialParam;
import org.dromara.daxpay.payment.merchant.result.config.MerchantCredentialResult;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 商户API配置服务
 * @author xxm
 * @since 2025/9/13
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantCredentialService {
    private final MerchantCredentialManager credentialManager;
    private final MerchantManager merchantManager;
    private final DaxPayProperties daxPayProperties;

    /**
     * 根据商户号查询
     */
    public MerchantCredentialResult findByMchNo(String mchNo) {
        String publicKey = daxPayProperties.getKeyConfig().getPublicKey();
        var credentialOptional = credentialManager.findByMchNo(mchNo);
        if (credentialOptional.isEmpty()){
            var merchant = merchantManager.findByMchNo(mchNo)
                    .orElseThrow(() -> new DataNotExistException("商户不存在"));
            var credential = new MerchantCredential();
            credential.setMchNo(mchNo).setIsvNo(merchant.getIsvNo());
            credentialManager.save(credential);
            return credential.toResult().setPlatformPublicKey(publicKey);
        }
        return credentialOptional.get().toResult().setPlatformPublicKey(publicKey);
    }

    /**
     * 更新
     */
    public void update(MerchantCredentialParam param) {
        var credential = credentialManager.findByMchNo(param.getMchNo())
                .orElseThrow(() -> new DataNotExistException("商户API配置不存在"));
        // 判断公钥是否合法
        if (StrUtil.isNotBlank(param.getPublicKey())){
            RsaSignUtil.loadPublicKeyFromPem(param.getPublicKey());
        }
        MerchantCredentialConvert.CONVERT.copy(param, credential);
        credentialManager.updateById(credential);
    }
}
