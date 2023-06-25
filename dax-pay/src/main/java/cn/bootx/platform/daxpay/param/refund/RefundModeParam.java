package cn.bootx.platform.daxpay.param.refund;

import cn.bootx.platform.daxpay.code.pay.PayChannelEnum;
import cn.bootx.platform.daxpay.dto.payment.RefundableInfo;
import cn.bootx.platform.daxpay.param.pay.PayWayParam;
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
    @Schema(description = "支付通道")
    private String payChannel;

    @Schema(description = "支付金额")
    private BigDecimal amount;

    /**
     * 转换成支付方式参数
     */
    public PayWayParam toPayModeParam() {
        return new PayWayParam().setPayChannel(getPayChannel()).setAmount(getAmount());
    }

    /**
     * 转换成退款方式记录对象
     */
    public RefundableInfo toRefundableInfo() {
        return new RefundableInfo().setPayChannel(getPayChannel()).setAmount(getAmount());
    }

}
