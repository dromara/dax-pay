package cn.daxpay.open.payment.merchant.service.query;

import cn.daxpay.open.payment.common.service.MerchantUserQueryService;
import cn.daxpay.open.payment.merchant.service.user.MerchantUserService;
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
