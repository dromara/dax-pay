package org.dromara.daxpay.payment.merchant.param.config;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 商户产品配置项
///
@Data
@Accessors(chain = true)
@Schema(title = "商户产品配置项")
public class MchProductConfigItem {

    @Schema(description = "产品编码")
    private String product;

    @Schema(description = "通道编码")
    private String channel;

    @Schema(description = "是否启用")
    private Boolean enable;
}
