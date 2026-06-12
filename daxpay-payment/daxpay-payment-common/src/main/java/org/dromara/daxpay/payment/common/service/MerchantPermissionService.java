package org.dromara.daxpay.payment.common.service;

import java.util.List;

/// # 商户侧权限检查能力
///
/// 由端侧模块提供实现，供 bus 公共底座按最小能力依赖。
public interface MerchantPermissionService {

    /// 是否允许发起进件
    boolean canApplyMch();

    /// 是否允许退款
    boolean canRefund();

    /// 是否允许调用指定 API
    boolean hasApiPerm(String api);

    /// 是否允许使用指定通道
    boolean hasChannelPerm(String channel);

    /// 根据商户号获取可用通道
    List<String> getAvailableChannel(String mchNo);
}

