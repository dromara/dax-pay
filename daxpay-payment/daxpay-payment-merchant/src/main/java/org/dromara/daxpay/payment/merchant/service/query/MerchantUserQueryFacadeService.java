package org.dromara.daxpay.payment.merchant.service.query;

import org.dromara.daxpay.payment.common.service.MerchantUserQueryService;
import org.dromara.daxpay.payment.merchant.service.user.MerchantUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 商户用户归属查询门面
///
@Service
@RequiredArgsConstructor
public class MerchantUserQueryFacadeService implements MerchantUserQueryService {
    private final MerchantUserService merchantUserService;

    @Override
    public String findMchNoByUserId(Long userId) {
        return merchantUserService.findByUserId(userId);
    }
}
