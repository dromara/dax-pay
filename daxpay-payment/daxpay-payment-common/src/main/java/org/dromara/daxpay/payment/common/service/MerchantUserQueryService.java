package org.dromara.daxpay.payment.common.service;

/// # 商户用户归属查询服务
///
public interface MerchantUserQueryService {

    /// 根据用户id查询商户号
    String findMchNoByUserId(Long userId);
}
