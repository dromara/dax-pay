package cn.daxpay.open.payment.common.service;

import cn.daxpay.open.platform.core.rest.dto.LabelValue;

import java.util.List;

/// # 商户辅助查询服务
///
public interface MerchantAssistQueryService {
    List<LabelValue> dropdown();
    List<LabelValue> dropdownByEnable();
}
