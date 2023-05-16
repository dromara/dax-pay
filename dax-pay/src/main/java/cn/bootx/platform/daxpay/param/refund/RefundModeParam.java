package cn.bootx.platform.daxpay.param.refund;

import cn.bootx.platform.daxpay.code.pay.PayChannelCode;
import cn.bootx.platform.daxpay.dto.payment.RefundableInfo;
import cn.bootx.platform.daxpay.param.pay.PayModeParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 退款方式参数
 *
 * @author xxm
 * @date 2022/3/2
 */
@Data
@Accessors(chain = true)
@Schema(title = "退款方式参数")
public class RefundModeParam {

    /**
     * @see PayChannelCode
     */
    @Schema(description = "支付通道", required = true)
    private int payChannel;

    @Schema(description = "支付金额", required = true)
    private BigDecimal amount;

    /**
     * 转换成支付方式参数
     */
    public PayModeParam toPayModeParam() {
        return new PayModeParam().setPayChannel(getPayChannel()).setAmount(getAmount());
    }

    /**
     * 转换成退款方式记录对象
     */
    public RefundableInfo toRefundableInfo() {
        return new RefundableInfo().setPayChannel(getPayChannel()).setAmount(getAmount());
    }

}
