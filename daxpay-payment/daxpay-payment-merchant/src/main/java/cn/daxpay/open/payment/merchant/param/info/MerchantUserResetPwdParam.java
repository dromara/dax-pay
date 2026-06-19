package cn.daxpay.open.payment.merchant.param.info;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 商户用户密码重置
///
@Data
@Accessors(chain = true)
@Schema(title = "商户用户密码重置")
public class MerchantUserResetPwdParam {

    @Schema(description = "用户主键不可为空")
    @NotNull(message = "{validation.field.userId.notNull}")
    private Long userId;

    @Schema(description = "新密码不可为空")
    @NotBlank(message = "{validation.field.newPassword.notBlank}")
    private String newPassword;
}
