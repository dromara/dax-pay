package org.dromara.daxpay.payment.isv.result.gateway;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 网关支付配置
 * @author xxm
 * @since 2024/11/27
 */
@Data
@Accessors(chain = true)
@Schema(title = "网关支付配置")
public class IsvGatewayPayConfigResult {

    /** 主键 */
    @Schema(description = "主键")
    private Long id;

    /** 聚合二维码是否显示 */
    @Schema(description = "聚合二维码是否显示")
    private boolean aggregateShow;

    /** h5收银台自动升级聚合支付 */
    @Schema(description = "h5收银台自动升级聚合支付")
    private boolean h5AutoUpgrade;

    /** 服务商号 */
    @Schema(description = "服务商号")
    private String isvNo;
}
