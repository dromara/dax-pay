package cn.bootx.platform.daxpay.param.pay;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.param.channel.AliPayParam;
import cn.bootx.platform.daxpay.param.channel.VoucherPayParam;
import cn.bootx.platform.daxpay.param.channel.WalletPayParam;
import cn.bootx.platform.daxpay.param.channel.WeChatPayParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 简单退款参数，只可以用于非组合的支付订单
 * @author xxm
 * @since 2023/12/18
 */
@Data
@Schema(title = "简单退款参数")
public class SimpleRefundParam {

    @Schema(description = "支付单ID")
    private Long paymentId;

    @Schema(description = "业务号")
    private String businessNo;

    /**
     * 部分退款需要传输refundModes参数
     */
    @Schema(description = "是否全部退款")
    private boolean refundAll;

    /**
     * 部分退款时此项必填
     */
    @Schema(description = "退款订单号")
    private String refundNo;
    /**
     * @see PayChannelEnum#getCode()
     */
    @Schema(description = "支付通道编码")
    @NotBlank(message = "支付通道编码不可为空")
    private String payChannel;

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
}
