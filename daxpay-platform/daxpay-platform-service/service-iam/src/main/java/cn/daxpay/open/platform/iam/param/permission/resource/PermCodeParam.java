package cn.daxpay.open.platform.iam.param.permission.resource;

import cn.daxpay.open.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 权限码
///
@Data
@Accessors(chain = true)
@Schema(title = "权限码")
public class PermCodeParam {

    @NotNull(message = "{validation.field.id.notNull}", groups = {ValidationGroup.edit.class})
    @Schema(description = "id")
    private Long id;

    @NotBlank(message = "{validation.field.code.notBlank}")
    @Schema(description = "权限码")
    private String code;

    @NotBlank(message = "{validation.field.menuCode.notBlank}")
    @Schema(description = "菜单编码")
    private String menuCode;

    @Schema(description = "备注")
    private String remark;
}
