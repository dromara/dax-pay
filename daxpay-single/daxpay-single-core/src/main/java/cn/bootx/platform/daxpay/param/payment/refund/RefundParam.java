package cn.bootx.platform.daxpay.param.payment.refund;

import cn.bootx.platform.daxpay.param.PaymentCommonParam;
import cn.bootx.platform.daxpay.param.channel.AliPayParam;
import cn.bootx.platform.daxpay.param.channel.WalletPayParam;
import cn.bootx.platform.daxpay.param.channel.WeChatPayParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Map;

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
    private String bizRefundNo;

    /**
     * 支付订单号，与商户订单号至少要传输一个，同时传输以订单号为准
     */
    @Schema(description = "订单号")
    private String orderNo;

    /**
     * 商户支付订单号，与订单号至少要传输一个，同时传输以订单号为准
     */
    @Schema(description = "商户订单号")
    private String bizOrderNo;

    /** 退款金额 */
    @Schema(description = "退款金额")
    @Min(value = 1,message = "退款金额至少为0.01元")
    private Integer amount;

    /**
     * 预留的退款扩展参数
     * @see AliPayParam
     * @see WeChatPayParam
     * @see WalletPayParam
     */
    @Schema(description = "退款扩展参数")
    private Map<String, Object> extraParam;

    /** 退款原因 */
    @Schema(description = "退款原因")
    private String reason;

    /** 商户扩展参数,回调时会原样返回 */
    @Schema(description = "商户扩展参数,回调时会原样返回")
    private String attach;

    /** 是否不启用异步通知 */
    @Schema(description = "是否不启用异步通知")
    private Boolean notNotify;

    /** 异步通知地址 */
    @Schema(description = "异步通知地址")
    private String notifyUrl;

}
