package cn.daxpay.open.plugin.easypay.result.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(title = "易支付订单状态(内部)")
public class EasyPayOrderStatusResult {
    private Integer status;
    private String returnUrl;
    private String tradeNo;
    private String outTradeNo;
}
