package org.dromara.daxpay.service.result.allocation;

import org.dromara.daxpay.service.common.result.MchResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 分账配置
 * @author xxm
 * @since 2024/12/9
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "分账配置")
public class AllocConfigResult extends MchResult {

    @Schema(description = "是否自动分账")
    private Boolean autoAlloc;

    @Schema(description = "分账起始额")
    private BigDecimal minAmount;

    /** 自动完结 */
    @Schema(description = "是否自动完结")
    private Boolean autoFinish;

    /** 分账延迟时长(分钟) */
    @Schema(description = "分账延迟时长(分钟)")
    private Integer delayTime;
}
