package org.dromara.daxpay.payment.merchant.param.gateway;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 网关支付有配置
 * @author xxm
 * @since 2024/11/27
 */
@Data
@Accessors(chain = true)
@Schema(title = "收银台配置")
public class GatewayPayConfigParam {

    /** 是否读取系统配置 */
    @NotNull(message = "是否读取系统配置不可为空")
    @Schema(description = "是否读取系统配置")
    private boolean readSystem;

    /** h5是否读取系统配置 */
    @Schema(description = "h5是否读取系统配置")
    private boolean h5ReadSystem;

    /** pc是否读取系统配置 */
    @Schema(description = "pc是否读取系统配置")
    private boolean pcReadSystem;

    /** 聚合二维码是否读取系统配置 */
    @Schema(description = "聚合二维码是否读取系统配置")
    private boolean aggregateQrReadSystem;

    /** 聚合条码是否读取系统配置 */
    @Schema(description = "聚合条码是否读取系统配置")
    private boolean aggregateBarReadSystem;

    /** 小程序是否读取系统配置 */
    @Schema(description = "小程序是否读取系统配置")
    private boolean miniQuicklyReadSystem;

    /** 聚合二维码是否显示 */
    @Schema(description = "聚合二维码是否显示")
    private boolean aggregateQrShow;

    /** h5收银台自动升级聚合支付 */
    @Schema(description = "h5收银台自动升级聚合支付")
    private boolean h5AutoUpgrade;

    /** 应用号 */
    @NotBlank(message = "应用号不可为空")
    @Size(max = 32, message = "应用号不可超过32位")
    @Schema(description = "应用号")
    private String appId;
}
