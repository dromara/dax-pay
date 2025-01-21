package org.dromara.daxpay.channel.wechat.result.assist;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 微信AccessToken和OpenId
 * @author xxm
 * @since 2024/2/10
 */
@Data
@Accessors(chain = true)
@Schema(title = "微信AccessToken和OpenId")
public class WxTokenAndOpenIdResult {

    @Schema(description = "微信AccessToken")
    private String accessToken;

    @Schema(description = "微信用户唯一标识")
    private String openId;
}
