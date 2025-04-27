package org.dromara.daxpay.core.param.trade.pay;

import org.dromara.daxpay.core.param.PaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 支付状态同步参数
 * @author xxm
 * @since 2023/12/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "支付订单信息同步参数")
public class PaySyncParam extends PaymentCommonParam {

    /**
     * 支付订单号/商户订单号/通道订单号至少要传输一个，支付订单号 > 商户订单号 > 通道订单号
     */
    @Schema(description = "订单号")
    @Size(max = 100, message = "支付订单号不可超过100位")
    private String orderNo;

    /**
     * 支付订单号/商户订单号/通道订单号至少要传输一个，支付订单号 > 商户订单号 > 通道订单号
     */
    @Schema(description = "商户订单号")
    @Size(max = 100, message = "商户支付订单号不可超过100位")
    private String bizOrderNo;

    /**
     * 支付订单号/商户订单号/通道订单号至少要传输一个，支付订单号 > 商户订单号 > 通道订单号
     */
    @Schema(description = "通道订单号")
    @Size(max = 150, message = "通道支付订单号不可超过150位")
    private String outOrderNo;

}
