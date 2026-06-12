package org.dromara.daxpay.payment.channel.param.apply;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 商户申请查询参数
///
@Data
@Accessors(chain = true)
@Schema(title = "商户申请查询参数")
public class OnbMchApplyQuery {

    @Schema(description = "商户号")
    private String mchNo;
}
