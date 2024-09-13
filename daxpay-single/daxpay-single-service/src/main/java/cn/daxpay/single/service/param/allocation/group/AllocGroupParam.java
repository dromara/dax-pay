package cn.daxpay.single.service.param.allocation.group;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 分账组参数
 * @author xxm
 * @since 2024/4/1
 */
@Data
@Accessors(chain = true)
@Schema(title = "分账组参数")
public class AllocGroupParam {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "分账组编号")
    private String groupNo;

    @Schema(description = "分组名称")
    private String name;

    @Schema(description = "通道")
    private String channel;

    @Schema(description = "备注")
    private String remark;

}
