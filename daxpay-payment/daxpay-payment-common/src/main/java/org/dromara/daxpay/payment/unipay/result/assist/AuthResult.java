package org.dromara.daxpay.payment.unipay.result.assist;

import org.dromara.daxpay.platform.core.enums.unipay.ChannelAuthStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 认证结果
///
@Data
@Accessors(chain = true)
@Schema(title = "认证结果")
public class AuthResult {

    @Schema(description = "OpenId")
    private String openId;

    /// 支付宝存量商户部分返回的是用户ID
    @Schema(description = "用户ID")
    private String userId;

    /// 微信会返回accessToken，用于获取用户信息
    @Schema(description = "AccessToken")
    private String accessToken;

    /// 状态
    /// @see ChannelAuthStatusEnum
    @Schema(description = "状态")
    private String status;
}

