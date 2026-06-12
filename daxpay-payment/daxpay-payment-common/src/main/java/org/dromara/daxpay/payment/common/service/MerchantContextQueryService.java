package org.dromara.daxpay.payment.common.service;

/// # 商户上下文查询服务
///
public interface MerchantContextQueryService {

    /// 根据用户id查询商户号
    String findMchNoByUserId(Long userId);
}
