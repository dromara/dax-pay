package org.dromara.daxpay.core.param.trade.refund;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.daxpay.core.param.PaymentCommonParam;

import java.math.BigDecimal;


/**
 * 退款参数，适用于组合支付的订单退款操作中，
 * @author xxm
 * @since 2023/12/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "退款参数")
public class RefundParam extends PaymentCommonParam {

    /**
     * 商户退款号，不可以为空，同样的商户退款号多次请求，同一退款单号多次请求只退一笔
     */
    @Schema(description = "商户退款号")
    @NotBlank(message = "商户退款号不可为空")
    @Size(max = 100, message = "商户退款号不可超过100位")
    private String bizRefundNo;

    /**
     * 支付订单号，与商户订单号至少要传输一个，同时传输以订单号为准
     */
    @Schema(description = "订单号")
    @Size(max = 32, message = "支付订单号不可超过32位")
    private String orderNo;

    /**
     * 商户支付订单号，与订单号至少要传输一个，同时传输以订单号为准
     */
    @Schema(description = "商户订单号")
    @Size(max = 100, message = "商户订单号不可超过100位")
    private String bizOrderNo;

    /** 退款金额 */
    @Schema(description = "退款金额")
    @NotNull(message = "退款金额不可为空")
    @DecimalMin(value = "0.01", message = "支付金额不可小于0.01元")
    @Digits(integer = 8, fraction = 2, message = "支付金额精度到分, 且要小于一亿元")
    private BigDecimal amount;

    /**
     * 预留的退款扩展参数
     */
    @Schema(description = "退款扩展参数")
    @Size(max = 2048, message = "退款扩展参数不可超过2048位")
    private String extraParam;

    /** 退款原因 */
    @Schema(description = "退款原因")
    @Size(max = 150, message = "退款原因不可超过150位")
    private String reason;

    /** 商户扩展参数,回调时会原样返回 */
    @Schema(description = "商户扩展参数")
    @Size(max = 500, message = "商户扩展参数不可超过500位")
    private String attach;

    /** 异步通知地址 */
    @Schema(description = "异步通知地址")
    @Size(max = 200, message = "异步通知地址不可超过200位")
    private String notifyUrl;

}
