package cn.daxpay.open.payment.merchant.param.info;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 商户忘记密码参数
///
/// 公开接口: 须同时校验账号与绑定手机号, 防止仅凭账号重置密码。
@Data
@Accessors(chain = true)
@Schema(title = "商户忘记密码参数")
public class MerchantForgotParam {

    @NotBlank(message = "{validation.field.account.notBlank}")
    @Schema(description = "用户账号")
    private String account;

    @NotBlank(message = "{validation.field.password.notBlank}")
    @Schema(description = "新密码")
    private String newPassword;

    @NotBlank(message = "{validation.field.phone.notBlank}")
    @Schema(description = "用户绑定手机号")
    private String phone;
}
