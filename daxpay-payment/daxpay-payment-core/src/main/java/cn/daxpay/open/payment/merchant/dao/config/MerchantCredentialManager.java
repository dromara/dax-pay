package cn.daxpay.open.payment.merchant.dao.config;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.payment.merchant.entity.config.MerchantCredential;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 商户凭证管理
///
@Repository
public class MerchantCredentialManager extends BaseManager<MerchantCredentialMapper, MerchantCredential> {

    /// 根据商户号查询
    public Optional<MerchantCredential> findByMchNo(String mchNo){
        return findByField(MerchantCredential::getMchNo, mchNo);
    }

    /// 按商户号脱敏改写(主体注销前置): 应用加密公私钥置空, 防离线解密残留
    public void sanitizeByMchNo(String mchNo) {
        lambdaUpdate()
                .set(MerchantCredential::getPublicKey, null)
                .set(MerchantCredential::getSecretKey, null)
                .eq(MerchantCredential::getMchNo, mchNo)
                .update();
    }
}
