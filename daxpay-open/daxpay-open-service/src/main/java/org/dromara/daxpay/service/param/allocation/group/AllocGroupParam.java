package org.dromara.daxpay.service.param.allocation.group;

import cn.bootx.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "主键不可为空", groups = {ValidationGroup.edit.class})
    @Schema(description = "主键")
    private Long id;

    @NotBlank(message = "分组名称不可为空")
    @Schema(description = "分组名称")
    private String name;

    @NotBlank(message = "通道不可为空")
    @Schema(description = "通道")
    private String channel;

    @Schema(description = "备注")
    private String remark;

    @NotBlank(message = "商户应用ID不可为空")
    @Schema(description = "商户应用ID")
    private String appId;
}
