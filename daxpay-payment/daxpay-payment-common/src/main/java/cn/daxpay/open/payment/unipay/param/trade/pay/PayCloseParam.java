package cn.daxpay.open.payment.unipay.param.trade.pay;

import cn.daxpay.open.payment.unipay.param.MerchantPaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/// # 支付关闭参数
///
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "支付关闭参数")
public class PayCloseParam extends MerchantPaymentCommonParam {

    /// 支付订单号/商户订单号/通道订单号至少要传输一个，支付订单号 > 商户订单号 > 通道订单号
    @Schema(description = "订单号")
    @Size(max = 100, message = "{validation.field.orderNo.size}")
    private String orderNo;

    /// 支付订单号/商户订单号/通道订单号至少要传输一个，支付订单号 > 商户订单号 > 通道订单号
    @Schema(description = "商户订单号")
    @Size(max = 100, message = "{validation.field.bizOrderNo.size}")
    private String bizOrderNo;

    /// 是否使用撤销方式进行订单关闭, 只有部分支付通道的支付方式才可以使用,
    /// 如果支付订单不支持撤销, 这个参数将不会生效
    @Schema(description = "是否使用撤销方式进行订单关闭")
    private boolean useCancel;
}

