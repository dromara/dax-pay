package cn.daxpay.open.payment.unipay.param.trade.pay;

import cn.daxpay.open.payment.unipay.param.MerchantPaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/// # 支付状态同步参数
///
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "支付订单信息同步参数")
public class PaySyncParam extends MerchantPaymentCommonParam {

    /// 支付订单号/商户订单号/通道订单号至少要传输一个，支付订单号 > 商户订单号 > 通道订单号
    @Schema(description = "订单号")
    @Size(max = 100, message = "{validation.field.orderNo.size}")
    private String orderNo;

    /// 支付订单号/商户订单号/通道订单号至少要传输一个，支付订单号 > 商户订单号 > 通道订单号
    @Schema(description = "商户订单号")
    @Size(max = 100, message = "{validation.field.bizOrderNo.size}")
    private String bizOrderNo;

    /// 支付订单号/商户订单号/通道订单号至少要传输一个，支付订单号 > 商户订单号 > 通道订单号
    @Schema(description = "通道订单号")
    @Size(max = 150, message = "{validation.field.outOrderNo.size}")
    private String outOrderNo;

}
