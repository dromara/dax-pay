package org.dromara.daxpay.sdk.param.channel.wechat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 微信支付参数
 * @author xxm
 * @since 2021/6/21
 */
@Getter
@Setter
@Accessors(chain = true)
public class WechatPayParam {

    /**
     * 服务商模式下分为 用户服务标识 sp 用户子标识 sub
     */
    @Schema(description = "openid类型")
    private String openIdType;

}
