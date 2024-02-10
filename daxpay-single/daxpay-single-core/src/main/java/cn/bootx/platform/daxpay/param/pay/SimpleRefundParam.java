package cn.bootx.platform.daxpay.param.pay;

import cn.bootx.platform.daxpay.param.PaymentCommonParam;
import cn.bootx.platform.daxpay.param.channel.AliPayParam;
import cn.bootx.platform.daxpay.param.channel.VoucherPayParam;
import cn.bootx.platform.daxpay.param.channel.WalletPayParam;
import cn.bootx.platform.daxpay.param.channel.WeChatPayParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 简单退款参数，只可以用于非组合的支付订单
 * @author xxm
 * @since 2023/12/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "简单退款参数")
public class SimpleRefundParam extends PaymentCommonParam {

    /**
     * 优先级高于业务号
     */
    @Schema(description = "支付单ID")
    private Long paymentId;

    @Schema(description = "业务号")
    private String businessNo;

    /**
     * 部分退款时此项必填, 不传输系统会自动生成
     */
    @Schema(description = "退款订单号")
    private String refundNo;

    /**
     * 部分退款需要传输refundModes参数
     */
    @Schema(description = "是否全部退款")
    private boolean refundAll;


    @Schema(description = "退款金额")
    @NotNull(message = "退款金额不可为空")
    private Integer amount;

    /**
     * 预留的扩展参数, 暂时未使用
     * @see AliPayParam
     * @see WeChatPayParam
     * @see VoucherPayParam
     * @see WalletPayParam
     */
    @Schema(description = "附加退款参数")
    private String channelExtra;

    @Schema(description = "退款原因")
    private String reason;


    /** 商户扩展参数,回调时会原样返回 */
    @Schema(description = "商户扩展参数,回调时会原样返回")
    private String attach;

    /** 是否不启用异步通知 */
    @Schema(description = "是否不启用异步通知")
    private boolean notNotify;

    /** 异步通知地址 */
    @Schema(description = "异步通知地址")
    private String notifyUrl;
}
