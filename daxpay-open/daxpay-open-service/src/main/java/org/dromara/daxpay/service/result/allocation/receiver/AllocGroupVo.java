package org.dromara.daxpay.service.result.allocation.receiver;

import org.dromara.daxpay.service.common.result.MchResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 分账组
 * @author xxm
 * @since 2024/4/1
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "分账组")
public class AllocGroupVo extends MchResult {

    @Schema(description = "分账组编号")
    private String groupNo;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "通道")
    private String channel;

    @Schema(description = "默认分账组")
    private Boolean defaultGroup;

    @Schema(description = "分账比例(百分之多少)")
    private BigDecimal totalRate;

    @Schema(description = "备注")
    private String remark;
}
