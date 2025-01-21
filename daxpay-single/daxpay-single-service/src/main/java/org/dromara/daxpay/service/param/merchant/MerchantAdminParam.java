package org.dromara.daxpay.service.param.merchant;

import cn.bootx.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 商户管理员参数
 * @author xxm
 * @since 2024/8/23
 */
@Data
@Accessors(chain = true)
@Schema(title = "商户管理员参数")
public class MerchantAdminParam {

    @Schema(description = "主键")
    @NotNull(groups = ValidationGroup.edit.class, message = "主键不可为空")
    private Long id;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "登录账号")
    private String account;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "密码")
    private String password;
}
