package org.dromara.daxpay.payment.unipay.result.trade.pay;

import org.dromara.daxpay.platform.core.enums.unipay.PayBodyTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 统一支付响应参数
///
@Data
@Accessors(chain = true)
@Schema(title = "统一支付响应参数")
public class PayResult {

    /// 订单ID
    @Schema(description = "订单ID")
    private Long orderId;

    /// 商户订单号
    @Schema(description = "商户订单号")
    private String bizOrderNo;

    /// 订单号
    @Schema(description = "订单号")
    private String orderNo;

    /// 支付状态
    @Schema(description = "支付状态")
    private String status;

    /// 支付参数体(通常用于发起支付的参数)
    @Schema(description = "支付参数体")
    private String payBody;

    /// 支付参数体类型
    /// @see PayBodyTypeEnum
    @Schema(description = "支付参数体类型")
    private String payBodyType;
}

