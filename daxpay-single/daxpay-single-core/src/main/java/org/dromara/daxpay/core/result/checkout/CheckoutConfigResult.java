package org.dromara.daxpay.core.result.checkout;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 收银台配置
 * @author xxm
 * @since 2024/11/27
 */
@Data
@Accessors(chain = true)
@Schema(title = "收银台配置")
public class CheckoutConfigResult {
    /** 收银台名称 */
    @Schema(description = "收银台名称")
    private String name;

    /** PC收银台是否同时显示聚合收银码 */
    @Schema(description = "PC收银台是否同时显示聚合收银码")
    private boolean aggregateShow;

    /** h5收银台自动升级聚合支付 */
    @Schema(description = "h5收银台自动升级聚合支付")
    private boolean h5AutoUpgrade;
}
