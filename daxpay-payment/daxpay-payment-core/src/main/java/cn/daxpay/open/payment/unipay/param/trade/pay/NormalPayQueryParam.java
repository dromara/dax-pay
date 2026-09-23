package cn.daxpay.open.payment.unipay.param.trade.pay;

import cn.daxpay.open.payment.unipay.param.MerchantPaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/// # 支付单查询参数
///
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "支付单查询参数")
public class NormalPayQueryParam extends MerchantPaymentCommonParam {

    /// 平台支付订单号或商户订单号至少要传输一个。
    /// 注意: 此处 orderNo 接受的是**资金交易号(tradeNo)**(实现按 findByTradeNo 匹配),
    /// 与响应结果中的 orderNo(容器业务单号)不是同一个编号, 详见查询服务与接口文档说明。
    @Schema(description = "订单号(资金交易号tradeNo, 非响应中的容器orderNo)")
    @Size(max = 100, message = "{validation.field.orderNo.size}")
    private String orderNo;

    /// 商户订单号(orderNo 为空时按 商户号+商户订单号 定位)
    @Schema(description = "商户订单号")
    @Size(max = 100, message = "{validation.field.bizOrderNo.size}")
    private String bizOrderNo;
}
