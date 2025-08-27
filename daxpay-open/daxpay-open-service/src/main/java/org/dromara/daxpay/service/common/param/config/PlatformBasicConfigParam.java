package org.dromara.daxpay.service.common.param.config;

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
@Schema(title = "平台基础配置")
public class PlatformBasicConfigParam {

    /** 全局单笔限额 */
    @DecimalMin(value = "0.01", message = "支付限额不可小于0.01元")
    @Digits(integer = 10, fraction = 2, message = "最多只允许两位小数")
    @Schema(description = "每月累计限额")
    private BigDecimal singleLimitAmount;

    /** 每月累计限额 */
    @DecimalMin(value = "0.01", message = "每月累计限额不可小于0.01元")
    @Digits(integer = 10, fraction = 2, message = "最多只允许两位小数")
    @Schema(description = "每月累计限额")
    private BigDecimal monthlyLimitAmount;

    /** 每日限额 */
    @DecimalMin(value = "0.01", message = "每日限额不可小于0.01元")
    @Digits(integer = 10, fraction = 2, message = "最多只允许两位小数")
    @Schema(description = "每日限额")
    private BigDecimal dailyLimitAmount;

}
