package cn.bootx.platform.daxpay.sdk.param.channel;

import cn.bootx.platform.daxpay.sdk.param.ChannelParam;
import lombok.Getter;
import lombok.Setter;

/**
 * 储值卡支付参数
 *
 * @author xxm
 * @since 2022/3/14
 */
@Getter
@Setter
public class VoucherPayParam implements ChannelParam {

    /** 储值卡号 */
    private String cardNo;

}
