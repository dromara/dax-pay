package cn.daxpay.open.payment.merchant.param.info;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 商户忘记密码参数
///
@Data
@Accessors(chain = true)
@Schema(title = "商户忘记密码参数")
public class MerchantForgotParam {

    @Schema(description = "用户账号")
    private String account;

    @Schema(description = "新密码")
    private String newPassword;

    @Schema(description = "用户绑定手机号")
    private String phone;
}
