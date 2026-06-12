package org.dromara.daxpay.platform.iam.param.role;

import org.dromara.daxpay.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
@Schema(title = "角色")
public class RoleParam {

    @NotNull(groups = { ValidationGroup.edit.class }, message = "{validation.field.id.notNull}")
    @Schema(description = "角色id")
    private Long id;

    @NotBlank(message = "{validation.field.code.notBlank}")
    @Schema(description = "角色code")
    private String code;

    @NotBlank(message = "{validation.field.nameCn.notBlank}")
    @Schema(description = "中文名称")
    private String nameCn;

    @NotBlank(message = "{validation.field.nameEn.notBlank}")
    @Schema(description = "英文名称")
    private String nameEn;

    @NotBlank(message = "{validation.field.clientCode.notBlank}")
    @Schema(description = "终端编码")
    private String clientCode;

    @Schema(description = "描述")
    private String remark;

}
