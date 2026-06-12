package org.dromara.daxpay.payment.common.service.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 商户应用接入信息
///
@Data
@Accessors(chain = true)
public class MchAppInfoAccessInfo {

    /// 应用号
    private String appId;

    /// 商户号
    private String mchNo;

    /// 应用状态
    private String status;
}
