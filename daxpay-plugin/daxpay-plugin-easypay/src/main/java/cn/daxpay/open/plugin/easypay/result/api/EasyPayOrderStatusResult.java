package cn.daxpay.open.plugin.easypay.result.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 易支付订单状态（内部轮询）
///
@Data
@Accessors(chain = true)
@Schema(title = "易支付订单状态(内部)")
public class EasyPayOrderStatusResult {

    /// 协议状态 0待付 1成功
    @Schema(description = "协议状态 0待付 1成功")
    private Integer status;

    /// 同步跳转地址
    @Schema(description = "同步跳转地址")
    private String returnUrl;

    /// 平台业务单号
    @Schema(description = "平台业务单号")
    private String tradeNo;

    /// 商户订单号
    @Schema(description = "商户订单号")
    private String outTradeNo;
}
