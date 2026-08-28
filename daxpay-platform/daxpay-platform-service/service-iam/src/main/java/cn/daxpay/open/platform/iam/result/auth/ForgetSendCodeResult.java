package cn.daxpay.open.platform.iam.result.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 找回密码验证码发送结果
///
@Data
@Accessors(chain = true)
@Schema(title = "找回密码验证码发送结果")
public class ForgetSendCodeResult {

    @Schema(description = "找回流程ID(用于重置密码请求)")
    private String flowId;
}
