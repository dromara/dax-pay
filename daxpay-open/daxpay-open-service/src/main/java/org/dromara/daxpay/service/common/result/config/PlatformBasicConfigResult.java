package org.dromara.daxpay.service.common.result.config;

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
}
