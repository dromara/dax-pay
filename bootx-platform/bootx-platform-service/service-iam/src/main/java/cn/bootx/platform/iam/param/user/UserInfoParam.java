package cn.bootx.platform.iam.param.user;

import cn.bootx.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author xxm
 * @since 2021/6/2
 */
@Data
@Accessors(chain = true)
@Schema(title = "用户信息参数")
public class UserInfoParam {

    @Schema(description = "主键")
    @NotNull(groups = ValidationGroup.edit.class, message = "主键不可为空")
    private Long id;

    @NotBlank(groups = ValidationGroup.add.class, message = "名称不可为空")
    @Schema(description = "名称")
    private String name;

    @NotBlank(groups = ValidationGroup.add.class, message = "登录账号不可为空")
    @Schema(description = "登录账号")
    private String account;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @NotBlank(groups = ValidationGroup.add.class, message = "登录账号不可为空")
    @Schema(description = "密码")
    private String password;

}
