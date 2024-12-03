package org.dromara.daxpay.single.sdk.param.channel;

import lombok.Data;
import lombok.experimental.Accessors;
import org.dromara.daxpay.single.sdk.param.ChannelParam;

/**
 * 微信支付参数
 * @author xxm
 * @since 2021/6/21
 */
@Data
@Accessors(chain = true)
public class WechatPayParam implements ChannelParam {

    /** 微信openId */
    private String openId;

    /** 授权码(主动扫描用户的付款码) */
    private String authCode;

}
