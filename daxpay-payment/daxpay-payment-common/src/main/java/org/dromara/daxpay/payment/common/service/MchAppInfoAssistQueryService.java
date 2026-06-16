package org.dromara.daxpay.payment.common.service;

import org.dromara.daxpay.platform.core.rest.dto.LabelValue;

import java.util.List;

/// # 商户应用辅助查询服务
///
public interface MchAppInfoAssistQueryService {
    List<LabelValue> dropdown(String mchNo);
    List<LabelValue> dropdownByEnable(String mchNo);

    /// 查询商户默认应用号
    String findDefaultAppId(String mchNo);
}
