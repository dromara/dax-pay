package cn.daxpay.open.payment.common.access;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 商户接入信息
///
@Data
@Accessors(chain = true)
public class MerchantAccessInfo {

    /// 商户号
    private String mchNo;

    /// 商户状态
    private String status;

}
