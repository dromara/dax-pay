package cn.daxpay.open.platform.capability.wechat.auth.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 微信认证结果
///
@Data
@Accessors(chain = true)
@Schema(title = "微信认证结果")
public class WechatAuthResult {

    @Schema(description = "访问令牌")
    private String accessToken;

    @Schema(description = "OpenId")
    private String openId;
}
