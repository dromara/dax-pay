package cn.bootx.platform.daxpay.param.refund;

import cn.bootx.platform.daxpay.code.pay.PayChannelEnum;
import cn.bootx.platform.daxpay.dto.payment.RefundableInfo;
import cn.bootx.platform.daxpay.param.channel.voucher.VoucherRefundParam;
import cn.bootx.platform.daxpay.param.channel.wallet.WalletRefundParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 退款方式参数
 *
 * @author xxm
 * @since 2022/3/2
 */
@Data
@Accessors(chain = true)
@Schema(title = "退款方式参数")
public class RefundModeParam {

    /**
     * @see PayChannelEnum#getCode()
     */
    @Schema(description = "支付渠道")
    private String payChannel;

    @Schema(description = "支付金额")
    private BigDecimal amount;

    /**
     * @see VoucherRefundParam
     * @see WalletRefundParam
     */
    @Schema(description = "扩展参数的json字符串")
    private String extraParamsJson;


    /**
     * 转换成退款方式记录对象
     */
    public RefundableInfo toRefundableInfo() {
        return new RefundableInfo().setPayChannel(getPayChannel()).setAmount(getAmount());
    }

}
