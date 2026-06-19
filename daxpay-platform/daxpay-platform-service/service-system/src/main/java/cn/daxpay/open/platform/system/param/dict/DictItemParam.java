package cn.daxpay.open.platform.system.param.dict;

import cn.daxpay.open.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 字典项参数
///
@Data
@Accessors(chain = true)
@Schema(title = "字典项参数")
public class DictItemParam {

    @Null(message = "{validation.field.id.mustBeNullOnAdd}", groups = ValidationGroup.add.class)
    @NotNull(message = "{validation.field.id.notNull}", groups = ValidationGroup.edit.class)
    @Schema(description = "主键")
    private Long id;

    @NotNull(message = "{validation.field.dictId.notNull}")
    @Schema(description = "字典ID")
    private Long dictId;

    @Schema(description = "字典编码")
    private String dictCode;

    @NotBlank(message = "{validation.field.code.notBlank}", groups = ValidationGroup.add.class)
    @Schema(description = "字典项编码")
    private String code;

    @NotBlank(message = "{validation.field.nameCn.notBlank}", groups = ValidationGroup.add.class)
    @Schema(description = "中文名称")
    private String nameCn;

    @Schema(description = "英文名称")
    private String nameEn;

    @NotNull(message = "{validation.field.enable.notNull}", groups = ValidationGroup.add.class)
    @Schema(description = "启用状态")
    private Boolean enable;

    @Schema(description = "字典项排序")
    private Double sortNo;

    @Schema(description = "备注")
    private String remark;

}
