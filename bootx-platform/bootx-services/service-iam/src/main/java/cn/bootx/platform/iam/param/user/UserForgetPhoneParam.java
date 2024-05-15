package cn.bootx.platform.iam.param.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * 通过手机验证码忘记密码参数
 *
 * @author xxm
 * @since 2022/6/26
 */
@Data
@Accessors(chain = true)
@Schema(title = "通过手机验证码忘记密码参数")
public class UserForgetPhoneParam {

    @NotBlank(message = "手机号不得为空")
    @Schema(description = "手机号")
    private String phone;

    @NotBlank(message = "验证码不得为空")
    @Schema(description = "验证码")
    private String captcha;

    @NotBlank(message = "密码不得为空")
    @Schema(description = "密码")
    private String password;

}
