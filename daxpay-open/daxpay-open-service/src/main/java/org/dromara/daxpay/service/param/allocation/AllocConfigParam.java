package org.dromara.daxpay.service.param.allocation;

import cn.bootx.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "主键不可为空", groups = {ValidationGroup.edit.class})
    @Schema(description = "主键")
    private Long id;

    /** 是否自动分账 */
    @NotNull(message = "是否自动分账不可为空")
    @Schema(description = "是否自动分账")
    private Boolean autoAlloc;

    /** 自动完结 */
    @NotNull(message = "自动完结不可为空")
    @Schema(description = "自动完结")
    private Boolean autoFinish;

    /** 分账起始额 */
    @DecimalMin(value = "0.1", message = "分账起始额不可小于0.1元")
    @Schema(description = "分账起始额")
    private BigDecimal minAmount;

    /** 分账延迟时长(分钟) */
    @DecimalMin(value = "0", message = "分账延迟时长不能为负数")
    @Schema(description = "分账延迟时长(分钟)")
    private Integer delayTime;

    /** 应用AppId */
    @NotNull(message = "应用AppId不可为空")
    @Schema(description = "应用AppId")
    private String appId;

}
