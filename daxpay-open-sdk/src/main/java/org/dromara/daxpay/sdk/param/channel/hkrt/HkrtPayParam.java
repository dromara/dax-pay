package org.dromara.daxpay.sdk.param.channel.hkrt;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 海科支付参数
 * @author xxm
 * @since 2025/5/16
 */
@Data
@Accessors(chain = true)
@Schema(title = "海科支付参数")
public class HkrtPayParam {

    /** 微信公众账号ID */
    @Schema(description = "微信公众账号ID")
    private String wxAppId;

}
