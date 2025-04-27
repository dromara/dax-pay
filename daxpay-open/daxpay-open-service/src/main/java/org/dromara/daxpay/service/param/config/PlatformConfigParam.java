package org.dromara.daxpay.service.param.config;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 平台配置
 * @author xxm
 * @since 2024/9/19
 */
@Data
@Accessors(chain = true)
@Schema(title = "平台配置")
public class PlatformConfigParam {

    /** 支付网关地址 */
    @Schema(description = "支付网关地址")
    private String gatewayServiceUrl;

    /** 网关移动端地址 */
    @Schema(description = "网关移动端地址")
    private String gatewayMobileUrl;

    /** 全局单笔限额 */
    @DecimalMin(value = "0.01", message = "支付限额不可小于0.01元")
    @Digits(integer = 10, fraction = 2, message = "最多只允许两位小数")
    @Schema(description = "全局单笔限额")
    private BigDecimal limitAmount;

}
