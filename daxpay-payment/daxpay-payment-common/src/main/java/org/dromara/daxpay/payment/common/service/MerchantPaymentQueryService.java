package org.dromara.daxpay.payment.common.service;

import org.dromara.daxpay.payment.common.service.dto.MchAppInfoAccessInfo;
import org.dromara.daxpay.payment.common.service.dto.MerchantAccessInfo;

/// # 商户支付上下文查询服务
///
public interface MerchantPaymentQueryService {

    /// 根据商户号查询商户接入信息
    MerchantAccessInfo getMerchantByMchNo(String mchNo);

    /// 根据商户号查询默认应用
    MchAppInfoAccessInfo getDefaultAppByMchNo(String mchNo);

    /// 根据应用号查询应用
    MchAppInfoAccessInfo getAppByAppId(String appId);

    /// 根据商户号查询商户公钥
    String findMerchantPublicKey(String mchNo);
}
