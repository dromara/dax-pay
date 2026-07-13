package cn.daxpay.open.payment.unipay.result.assist;

import cn.daxpay.open.platform.core.enums.unipay.ChannelAuthStatusEnum;
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

    /// 来源回跳路径(会话恢复时回填), 前端授权完成后据此跳回业务页面
    @Schema(description = "来源回跳路径")
    private String returnPath;
}

