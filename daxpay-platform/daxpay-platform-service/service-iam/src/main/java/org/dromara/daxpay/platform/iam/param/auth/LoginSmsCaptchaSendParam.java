package org.dromara.daxpay.platform.iam.param.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 发送登录短信验证码参数
///
@Data
@Accessors(chain = true)
@Schema(title = "发送登录短信验证码参数")
public class LoginSmsCaptchaSendParam {

    @Schema(description = "终端Code")
    private String clientId;

    @NotBlank(message = "{validation.field.phone.notBlank}")
    @Schema(description = "手机号")
    private String phone;

}
