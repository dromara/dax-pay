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

    /**
     * 服务商模式下分为 1、用户服务标识 sp 2、用户子标识 sub
     */
    @Schema(description = "openid类型")
    private String openIdType;

}
