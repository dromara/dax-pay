package cn.daxpay.open.payment.merchant.service.permission;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/// # 商户权限检查服务
///
/// 开源版默认放行,后续可扩展为真实 RBAC。
@Service
public class MerchantPermissionService {

    public boolean hasApiPerm(String api) {
        return true;
    }

    public boolean hasChannelPerm(String channel) {
        return true;
    }

    public List<String> getAvailableChannel(String mchNo) {
        return Collections.emptyList();
    }
}
