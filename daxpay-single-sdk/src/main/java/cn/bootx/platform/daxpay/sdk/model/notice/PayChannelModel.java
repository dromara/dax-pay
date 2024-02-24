package cn.bootx.platform.daxpay.sdk.model.notice;

import cn.bootx.platform.daxpay.sdk.code.PayChannelEnum;
import cn.bootx.platform.daxpay.sdk.code.PayWayEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 支付通道信息
 * @author xxm
 * @since 2024/1/7
 */
@Getter
@Setter
@ToString
public class PayChannelModel {
    /**
     * 支付通道编码
     * @see PayChannelEnum#getCode()
     */
    private String channel;

    /**
     * 支付方式编码
     * @see PayWayEnum
     */
    private String way;

    /** 支付金额 */
    private Integer amount;

}
