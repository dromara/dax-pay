package org.dromara.daxpay.payment.common.service;

import org.dromara.daxpay.platform.core.rest.dto.LabelValue;

import java.util.List;

/// # 商户辅助查询服务
///
public interface MerchantAssistQueryService {
    List<LabelValue> dropdown();
    List<LabelValue> dropdownByEnable();
}
