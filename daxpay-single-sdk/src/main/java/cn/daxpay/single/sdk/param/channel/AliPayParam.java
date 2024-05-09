package cn.daxpay.single.sdk.param.channel;

import cn.daxpay.single.sdk.param.ChannelParam;
import lombok.Getter;
import lombok.Setter;

/**
 * 支付宝支付参数
 * @author xxm
 * @since 2021/2/27
 */
@Getter
@Setter
public class AliPayParam implements ChannelParam {

    /** 授权码(主动扫描用户的付款码) */
    private String authCode;

}
