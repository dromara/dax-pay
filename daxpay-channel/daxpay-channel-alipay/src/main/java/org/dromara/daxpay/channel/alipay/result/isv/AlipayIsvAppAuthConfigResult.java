package org.dromara.daxpay.channel.alipay.result.isv;

import org.dromara.daxpay.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付宝服务商应用授权认证配置
///
/// 支付宝服务商应用授权认证配置的返回结果对象，包含用户标识类型和授权回调地址。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付宝服务商应用授权认证配置")
public class AlipayIsvAppAuthConfigResult extends BaseResult {

    /// 支付宝服务商应用ID
    @Schema(description = "应用ID")
    private Long appId;

    /// 用户标识类型
    @Schema(description = "用户标识类型")
    private String userIdType;

    /// 授权回调地址
    @Schema(description = "授权回调地址")
    private String authCallbackUrl;
}
