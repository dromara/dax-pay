package org.dromara.daxpay.payment.masterdata.constants.product.result;

import org.dromara.daxpay.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付产品配置结果
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付产品配置结果")
public class PayProductConfigResult extends BaseResult {

    @Schema(description = "产品编码")
    private String product;

    @Schema(description = "产品名称")
    private String name;

    @Schema(description = "通道编码")
    private String channel;

    @Schema(description = "通道名称")
    private String channelName;

    @Schema(description = "是否支持沙箱环境")
    private boolean sandboxSupport;

    @Schema(description = "是否启用")
    private boolean enabled;

    @Schema(description = "生效环境: prod/sandbox")
    private String activeEnv;

    @Schema(description = "是否已配置参数")
    private boolean configured;

    /// 是否为服务商模式（true=服务商, false=直连）
    private boolean isv;
}
