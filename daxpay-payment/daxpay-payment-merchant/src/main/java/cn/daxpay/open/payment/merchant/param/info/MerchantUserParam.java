package cn.daxpay.open.payment.merchant.param.info;

import cn.daxpay.open.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 商户用户参数
///
@Data
@Accessors(chain = true)
@Schema(title = "商户用户参数")
public class MerchantUserParam {

    @Schema(description = "主键")
    @NotNull(groups = ValidationGroup.edit.class, message = "{validation.field.id.notNull}")
    private Long id;

    /// 商户号
    @NotBlank(groups = ValidationGroup.add.class, message = "{validation.field.mchNo.notBlank}")
    @Schema(description = "商户号")
    private String mchNo;

    @NotBlank(groups = ValidationGroup.add.class, message = "{validation.field.name.notBlank}")
    @Size(min = 3, max = 15, message = "{validation.field.name.size}")
    @Schema(description = "名称")
    private String name;

    @NotBlank(groups = ValidationGroup.add.class, message = "{validation.field.account.notBlank}")
    @Size(min = 6, max = 20, message = "{validation.field.account.size}")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "{validation.field.account.pattern}")
    @Schema(description = "登录账号")
    private String account;

    @NotBlank(groups = ValidationGroup.add.class, message = "{validation.field.password.notBlank}")
    @Schema(description = "密码")
    private String password;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "邮箱")
    private String email;
}
