package cn.daxpay.open.platform.iam.result.twofactor;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 双因素认证绑定初始化数据
///
/// 用户点击"立即绑定"后返回, 供前端渲染二维码与手动录入密钥。
/// 此时密钥尚未落库, 处于待确认状态, 前端需凭动态码调用确认接口完成绑定。
///
@Data
@Accessors(chain = true)
@Schema(title = "双因素认证绑定初始化数据")
public class TwoFactorSetupResult {

    @Schema(description = "TOTP 密钥(Base32, 仅供手动录入兜底)")
    private String secret;

    @Schema(description = "otpauth URI(供前端渲染二维码)")
    private String otpAuthUri;
}
