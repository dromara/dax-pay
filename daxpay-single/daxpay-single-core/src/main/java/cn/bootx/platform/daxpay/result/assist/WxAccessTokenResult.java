package cn.bootx.platform.daxpay.result.assist;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 微信AccessToken
 * @author xxm
 * @since 2024/2/10
 */
@Data
@Accessors(chain = true)
@Schema(title = "微信AccessToken")
public class WxAccessTokenResult {

    @Schema(description = "微信AccessToken")
    private String accessToken;

    @Schema(description = "微信用户唯一标识")
    private String openId;
}
