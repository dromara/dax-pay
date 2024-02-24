package cn.bootx.platform.daxpay.sdk.model.notice;

import cn.bootx.platform.daxpay.sdk.code.PayChannelEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 退款通道信息
 * @author xxm
 * @since 2024/2/22
 */
@Getter
@Setter
@ToString
public class RefundChannelModel {

    /**
     * 通道编码
     * @see PayChannelEnum#getCode()
     */
    private String channel;

    /**
     * 退款金额
     */
    private Integer amount;
}
