package cn.daxpay.multi.sdk.param.channel;

import cn.daxpay.multi.sdk.param.ChannelParam;
import lombok.Getter;
import lombok.Setter;

/**
 * 微信支付参数
 * @author xxm
 * @since 2021/6/21
 */
@Getter
@Setter
public class WeChatPayParam implements ChannelParam {

    /** 微信openId */
    private String openId;

    /** 授权码(主动扫描用户的付款码) */
    private String authCode;

}
