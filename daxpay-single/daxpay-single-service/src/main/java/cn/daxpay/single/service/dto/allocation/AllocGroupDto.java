package cn.daxpay.single.service.dto.allocation;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import cn.bootx.table.modify.annotation.DbColumn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 分账组
 * @author xxm
 * @since 2024/4/1
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "分账组")
public class AllocGroupDto extends BaseDto {

    @Schema(description = "分账组编号")
    private String groupNo;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "通道")
    private String channel;

    @DbColumn(comment = "默认分账组")
    private Boolean defaultGroup;

    @Schema(description = "总分账比例(万分之多少)")
    private Integer totalRate;

    @Schema(description = "备注")
    private String remark;
}
