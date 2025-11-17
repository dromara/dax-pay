package org.dromara.daxpay.payment.merchant.result.gateway;

import org.dromara.daxpay.payment.merchant.result.info.MchResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 网关支付配置
 * @author xxm
 * @since 2024/11/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "网关支付配置")
public class GatewayPayConfigResult extends MchResult {

    /** 聚合二维码是否显示 */
    @Schema(description = "聚合二维码是否显示")
    private boolean aggregateQrShow;

    /** h5收银台自动升级聚合支付 */
    @Schema(description = "h5收银台自动升级聚合支付")
    private boolean h5AutoUpgrade;
}
