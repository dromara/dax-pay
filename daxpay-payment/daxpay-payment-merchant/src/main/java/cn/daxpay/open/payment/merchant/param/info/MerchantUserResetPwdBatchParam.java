package cn.daxpay.open.payment.merchant.param.info;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// # 商户用户批量重置密码参数
///
@Data
@Accessors(chain = true)
@Schema(title = "商户用户批量重置密码参数")
public class MerchantUserResetPwdBatchParam {

    @Schema(description = "用户主键集合")
    @NotEmpty(message = "{validation.field.userIds.notEmpty}")
    private List<Long> userIds;

    @Schema(description = "新密码不可为空")
    @NotBlank(message = "{validation.field.newPassword.notBlank}")
    private String newPassword;
}
