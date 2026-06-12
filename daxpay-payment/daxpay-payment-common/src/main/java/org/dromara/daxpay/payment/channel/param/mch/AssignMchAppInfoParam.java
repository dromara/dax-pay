package org.dromara.daxpay.payment.channel.param.mch;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 进件商户信息分配给商户应用
///
@Data
@Accessors(chain = true)
@Schema(title = "进件商户信息分配给商户应用")
public class AssignMchAppInfoParam {
    @Schema(description = "进件商户id")
    private Long onbMchId;

    @Schema(description = "商户应用号")
    private String appId;
}
