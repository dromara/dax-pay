package cn.daxpay.open.platform.system.param.dict;

import cn.daxpay.open.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 数据字典目录参数
///
@Data
@Accessors(chain = true)
@Schema(title = "数据字典目录")
public class DictParam {

    @Null(message = "{validation.field.id.mustBeNullOnAdd}", groups = ValidationGroup.add.class)
    @NotNull(message = "{validation.field.id.notNull}", groups = ValidationGroup.edit.class)
    @Schema(description = "主键")
    private Long id;

    @NotBlank(message = "{validation.field.code.notBlank}", groups = ValidationGroup.add.class)
    @Schema(description = "编码")
    private String code;

    @NotBlank(message = "{validation.field.name.notBlank}", groups = ValidationGroup.add.class)
    @Schema(description = "名称")
    private String name;

    @Schema(description = "国际化key")
    private String i18nKey;

    @NotNull(message = "{validation.field.enable.notNull}", groups = ValidationGroup.add.class)
    @Schema(description = "启用状态")
    private Boolean enable;

    @Schema(description = "字典类型")
    private String dictType;

    @Schema(description = "是否内置")
    private Boolean internal;

    @Schema(description = "描述")
    private String remark;

}
