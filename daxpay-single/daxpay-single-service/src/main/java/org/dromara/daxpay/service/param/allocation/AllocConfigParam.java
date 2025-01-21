package org.dromara.daxpay.service.param.allocation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 分账配置参数
 * @author xxm
 * @since 2024/12/9
 */
@Data
@Accessors(chain = true)
@Schema(title = "分账配置参数")
public class AllocConfigParam {

    /** 主键 */
    @Schema(description = "主键")
    private Long id;

    /** 是否自动分账 */
    @Schema(description = "是否自动分账")
    private Boolean autoAlloc;

    /** 自动完结 */
    @Schema(description = "自动完结")
    private Boolean autoFinish;

    /** 分账起始额 */
    @Schema(description = "分账起始额")
    private BigDecimal minAmount;

    /** 分账延迟时长(分钟) */
    @Schema(description = "分账延迟时长(分钟)")
    private Integer delayTime;

    /** 应用AppId */
    @Schema(description = "应用AppId")
    private String appId;

}
