package cn.bootx.platform.baseapi.dto.dynamicform;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 动态表单
 *
 * @author xxm
 * @since 2022-07-28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "动态表单")
@Accessors(chain = true)
public class DynamicFormDto extends BaseDto {

    @Schema(description = "表单名称")
    private String name;

    @Schema(description = "表单键名")
    private String code;

    @Schema(description = "表单内容")
    private String value;

    @Schema(description = "备注")
    private String remark;

}
