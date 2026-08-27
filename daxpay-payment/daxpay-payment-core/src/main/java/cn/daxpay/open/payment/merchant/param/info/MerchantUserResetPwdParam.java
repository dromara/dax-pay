package cn.daxpay.open.payment.merchant.param.info;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "新密码(RSA 加密), 可选; 不传时由系统生成随机密码并在响应中返回明文")
    private String newPassword;
}
