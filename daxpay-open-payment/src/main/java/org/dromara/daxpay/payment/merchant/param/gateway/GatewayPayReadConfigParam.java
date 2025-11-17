package org.dromara.daxpay.payment.merchant.param.gateway;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 网关支付读取配置参数
 * @author xxm
 * @since 2025/10/14
 */
@Data
@Accessors(chain = true)
@Schema(title = "网关支付读取配置参数")
public class GatewayPayReadConfigParam {

    /** 网关支付是否读取系统 */
    @NotNull(message = "网关支付是否读取系统不可为空")
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

    /** 应用号 */
    @NotBlank(message = "应用号不可为空")
    @Size(max = 32, message = "应用号不可超过32位")
    @Schema(description = "应用号")
    private String appId;
}
