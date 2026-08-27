package cn.daxpay.open.payment.merchant.param.info;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "新密码(RSA 加密), 可选; 不传时为每个用户独立生成随机密码并在响应中返回明文")
    private String newPassword;
}
