package org.dromara.daxpay.payment.common.context;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 交易信息上下文
///
/// 包含支付/退款/转账等交易场景的参与方参数
@Data
@Accessors(chain = true)
public class TradeInfo {

    /// 商户号
    private String mchNo;

    /// 应用号
    private String appId;

    /// 服务商号
    private String isvNo;

}
