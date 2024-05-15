package cn.bootx.platform.iam.param.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * 通过邮件验证码忘记密码参数
 *
 * @author xxm
 * @since 2022/6/26
 */
@Data
@Accessors(chain = true)
@Schema(title = "通过邮件验证码忘记密码参数")
public class UserForgetEmailParam {

    @NotBlank(message = "邮箱不得为空")
    @Email(message = "请输入正确的邮箱")
    @Schema(description = "邮箱")
    private String email;

    @NotBlank(message = "验证码不得为空")
    @Schema(description = "验证码")
    private String captcha;

    @NotBlank(message = "密码不得为空")
    @Schema(description = "密码")
    private String password;

}
