package org.dromara.daxpay.payment.merchant.service.permission;

import org.dromara.daxpay.payment.common.service.MerchantPermissionService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/// # 商户权限检查服务实现
///
@Service
public class MerchantPermissionServiceImpl implements MerchantPermissionService {

    @Override
    public boolean hasApiPerm(String api) {
        return true;
    }

    @Override
    public boolean hasChannelPerm(String channel) {
        return true;
    }

    @Override
    public List<String> getAvailableChannel(String mchNo) {
        return Collections.emptyList();
    }
}
