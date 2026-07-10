package cn.daxpay.open.channel.wechat.param.isv;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 微信服务商应用授权认证配置保存参数
///
@Data
@Accessors(chain = true)
@Schema(title = "微信服务商应用授权认证配置保存参数")
public class WechatIsvAppAuthConfigParam {

    @NotNull(message = "{validation.field.wechatIsvAppId.notNull}")
    @Schema(description = "微信服务商应用ID")
    private Long wechatIsvAppId;

    @Schema(description = "应用密钥(加密存储)，所有应用类型首次保存时必填")
    private String appSecret;
}
