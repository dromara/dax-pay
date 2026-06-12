package org.dromara.daxpay.payment.merchant.param.info;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// # 商户用户批量操作参数
///
@Data
@Accessors(chain = true)
@Schema(title = "商户用户批量操作参数")
public class MerchantUserBatchParam {

    @Schema(description = "用户主键集合")
    @NotEmpty(message = "{validation.field.userIds.notEmpty}")
    private List<Long> userIds;
}
