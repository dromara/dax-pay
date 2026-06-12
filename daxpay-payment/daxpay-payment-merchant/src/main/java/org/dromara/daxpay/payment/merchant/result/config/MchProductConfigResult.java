package org.dromara.daxpay.payment.merchant.result.config;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 商户产品配置
///
@Data
@Accessors(chain = true)
@Schema(title = "商户产品配置")
public class MchProductConfigResult {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "产品编码")
    private String product;

    @Schema(description = "产品名称")
    private String name;

    @Schema(description = "通道编码")
    private String channel;

    @Schema(description = "通道名称")
    private String channelName;

    @Schema(description = "商户号")
    private String mchNo;

    @Schema(description = "是否启用")
    private Boolean enable;
}
