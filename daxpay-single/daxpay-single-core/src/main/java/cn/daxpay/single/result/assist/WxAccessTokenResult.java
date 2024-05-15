package cn.daxpay.single.result.assist;

import cn.daxpay.single.result.PaymentCommonResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 微信AccessToken
 * @author xxm
 * @since 2024/2/10
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "微信AccessToken")
public class WxAccessTokenResult extends PaymentCommonResult {

    @Schema(description = "微信AccessToken")
    private String accessToken;

    @Schema(description = "微信用户唯一标识")
    private String openId;
}
