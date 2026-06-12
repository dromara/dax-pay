package org.dromara.daxpay.payment.merchant.dao.config;

import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.payment.merchant.entity.config.MerchantCredential;
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
}
