package org.dromara.daxpay.service.param.config.checkout;

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
public class CheckoutConfigParam {

    /** 主键 */
    @Schema(description = "主键")
    private Long id;

    /** 收银台名称 */
    @Schema(description = "收银台名称")
    private String name;

    /** PC收银台是否同时显示聚合收银码 */
    @Schema(description = "PC收银台是否同时显示聚合收银码")
    private boolean aggregateShow;

    /** h5收银台自动升级聚合支付 */
    @Schema(description = "h5收银台自动升级聚合支付")
    private boolean h5AutoUpgrade;

    /** 应用号 */
    @Schema(description = "应用号")
    private String appId;
}
