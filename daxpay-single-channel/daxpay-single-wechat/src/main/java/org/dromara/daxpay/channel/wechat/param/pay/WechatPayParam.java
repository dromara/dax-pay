package org.dromara.daxpay.channel.wechat.param.pay;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 微信支付参数
 * @author xxm
 * @since 2021/6/21
 */
@Data
@Accessors(chain = true)
@Schema(title = "微信支付参数")
public class WechatPayParam {

    @Schema(description = "微信openId")
    private String openId;

    @Schema(description = "授权码(主动扫描用户的付款码)")
    private String authCode;

}
