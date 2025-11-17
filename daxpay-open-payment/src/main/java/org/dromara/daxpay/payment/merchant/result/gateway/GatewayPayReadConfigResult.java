package org.dromara.daxpay.payment.merchant.result.gateway;

import org.dromara.daxpay.payment.merchant.result.info.MchResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 网关支付读取配置结果
 * @author xxm
 * @since 2025/10/14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "网关支付读取配置结果")
public class GatewayPayReadConfigResult extends MchResult {

    /** 网关支付是否读取系统 */
    @Schema(description = "网关支付是否读取系统")
    private boolean gatewayReadSystem;

    /** H5收银台读取系统 */
    @Schema(description = "H5收银台读取系统")
    private boolean h5ReadSystem;

    /** Pc收银台读取系统 */
    @Schema(description = "Pc收银台读取系统")
    private boolean pcReadSystem;

    /** 聚合扫码支付读取系统 */
    @Schema(description = "聚合扫码支付读取系统")
    private boolean aggregateQrReadSystem;

    /** 聚合付款码支付读取系统 */
    @Schema(description = "聚合付款码支付读取系统")
    private boolean aggregateBarReadSystem;

    /** 小程序快捷支付读取系统 */
    @Schema(description = "小程序快捷支付读取系统")
    private boolean miniQuicklyReadSystem;
}
