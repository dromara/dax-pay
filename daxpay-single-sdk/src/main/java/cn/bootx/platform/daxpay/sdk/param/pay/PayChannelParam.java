package cn.bootx.platform.daxpay.sdk.param.pay;

import cn.bootx.platform.daxpay.sdk.code.PayChannelEnum;
import cn.bootx.platform.daxpay.sdk.code.PayWayEnum;
import cn.bootx.platform.daxpay.sdk.param.ChannelParam;
import cn.bootx.platform.daxpay.sdk.param.channel.AliPayParam;
import cn.bootx.platform.daxpay.sdk.param.channel.WalletPayParam;
import cn.bootx.platform.daxpay.sdk.param.channel.WeChatPayParam;
import lombok.Getter;
import lombok.Setter;

/**
 * 同意下单支付方式参数
 *
 * @author xxm
 * @since 2020/12/8
 */
@Getter
@Setter
public class PayChannelParam {

    /**
     * 支付通道编码
     * @see PayChannelEnum#getCode()
     */
    private String channel;

    /**
     * 支付方式编码
     * @see PayWayEnum#getCode()
     */
    private String way;

    /** 支付金额 */
    private Integer amount;

    /**
     * 附加支付参数, 传输json格式字符串
     * @see AliPayParam
     * @see WeChatPayParam
     * @see VoucherPayParam
     * @see WalletPayParam
     */
    private ChannelParam channelParam;
}
