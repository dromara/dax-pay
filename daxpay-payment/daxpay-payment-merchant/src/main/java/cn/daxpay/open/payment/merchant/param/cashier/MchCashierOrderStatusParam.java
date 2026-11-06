package cn.daxpay.open.payment.merchant.param.cashier;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 商户收银订单状态查询参数
///
/// orderNo 与 bizOrderNo 至少传一个, 由服务层校验(两参数为组合约束, 不使用注解组合校验)。
@Data
@Accessors(chain = true)
@Schema(title = "商户收银订单状态查询参数")
public class MchCashierOrderStatusParam {

    /// 平台业务单号(优先)
    @Schema(description = "平台业务单号")
    private String orderNo;

    /// 商户业务单号(即收银端 clientOrderNo)
    @Schema(description = "商户业务单号")
    private String bizOrderNo;

    /// 是否主动向通道同步状态(仅待支付/支付中时生效)
    @Schema(description = "是否主动同步通道状态")
    private Boolean sync = false;

}
