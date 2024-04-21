package cn.bootx.platform.daxpay.sdk.param.refund;

import cn.bootx.platform.daxpay.sdk.code.PayChannelEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 分通道退款参数
 * @author xxm
 * @since 2023/12/18
 */
@Getter
@Setter
@ToString
@Deprecated
public class RefundChannelParam {

    /**
     * 支付通道编码
     * @see PayChannelEnum#getCode()
     */
    private String channel;

    /**
     * 退款金额
     */
    private Integer amount;

}
