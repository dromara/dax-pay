package org.dromara.daxpay.payment.common.result.config.platform;

import cn.bootx.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 平台配置
 * @author xxm
 * @since 2024/9/19
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "管理平台基础配置")
public class PlatformBasicConfigResult extends BaseResult {

    /** 全局单笔限额 */
    @Schema(description = "每月累计限额")
    private BigDecimal singleLimitAmount;

    /** 每月累计限额 */
    @Schema(description = "每月累计限额")
    private BigDecimal monthlyLimitAmount;

    /** 每日限额 */
    @Schema(description = "每日限额")
    private BigDecimal dailyLimitAmount;

    /** 订单超时时间(分钟) */
    @Schema(description = "订单超时时间(分钟)")
    private Integer orderTimeout;

    /** 默认服务商 */
    @Schema(description = "默认服务商")
    private String defaultIsvNo;
}
