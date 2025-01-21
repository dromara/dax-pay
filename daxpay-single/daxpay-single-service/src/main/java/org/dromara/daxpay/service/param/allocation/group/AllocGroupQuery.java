package org.dromara.daxpay.service.param.allocation.group;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.daxpay.service.common.param.MchAppQuery;

/**
 * 分账组参数
 * @author xxm
 * @since 2024/4/1
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "分账组参数")
public class AllocGroupQuery extends MchAppQuery {

    @Schema(description = "分账组编号")
    private String groupNo;

    @Schema(description = "分组名称")
    private String name;

    @Schema(description = "通道")
    private String channel;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "商户应用ID")
    private String appId;
}
