package cn.bootx.platform.baseapi.param.dynamicform;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 动态表单
 *
 * @author xxm
 * @since 2022-07-28
 */
@Data
@Schema(title = "动态表单")
@Accessors(chain = true)
public class DynamicFormParam {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "表单名称")
    private String name;

    @Schema(description = "表单键名")
    private String code;

    @Schema(description = "表单内容")
    private String value;

    @Schema(description = "备注")
    private String remark;

}
