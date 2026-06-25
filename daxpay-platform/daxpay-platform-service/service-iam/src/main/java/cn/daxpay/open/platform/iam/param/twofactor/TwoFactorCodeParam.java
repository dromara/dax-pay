package cn.daxpay.open.platform.iam.param.twofactor;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 双因素认证验证码参数
///
/// 绑定确认 / 关闭 / 重新生成备用码 均需提交验证码二次确认,
/// 关闭和重新生成支持 TOTP 动态码或备用码(BACKUP)两种类型。
///
@Data
@Accessors(chain = true)
@Schema(title = "双因素认证验证码参数")
public class TwoFactorCodeParam {

    @Schema(description = "验证码(TOTP 动态码或备用码)")
    @NotBlank(message = "{validation.field.twoFactorCode.notBlank}")
    private String code;

    @Schema(description = "验证码类型: TOTP(动态码, 默认) | BACKUP(备用码)")
    private String codeType;

    @Schema(description = "绑定初始化返回的密钥(仅绑定确认时必传, 其它场景从已绑定记录读取)")
    private String secret;
}
