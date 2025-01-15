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

    /**
     * 服务商模式下分为 sp. 用户服务标识 sub. 用户子标识
     * @see org.dromara.daxpay.channel.wechat.code.WechatPayCode#SP_OPENID
     */
    @Schema(description = "openid类型")
    private String openIdType;

    @Schema(description = "授权码(主动扫描用户的付款码)")
    private String authCode;

}
