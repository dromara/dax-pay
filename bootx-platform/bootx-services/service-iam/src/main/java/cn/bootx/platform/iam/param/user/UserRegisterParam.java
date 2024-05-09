package cn.bootx.platform.iam.param.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * 注册用户参数
 *
 * @author xxm
 * @since 2022/6/26
 */
@Data
@Accessors(chain = true)
@Schema(title = "注册用户参数")
public class UserRegisterParam {

    @NotBlank
    @Schema(description = "登录账号")
    private String username;

    @NotBlank
    @Schema(description = "密码")
    private String password;

    @NotBlank
    @Schema(description = "验证码key")
    private String captchaKey;

    @NotBlank
    @Schema(description = "验证码")
    private String captcha;

}
