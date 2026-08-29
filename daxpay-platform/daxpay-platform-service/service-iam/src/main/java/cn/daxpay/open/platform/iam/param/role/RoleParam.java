package cn.daxpay.open.platform.iam.param.role;

import cn.daxpay.open.platform.core.validation.ValidationGroup;
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

    @Schema(description = "国际化key")
    private String i18nKey;

    @NotBlank(message = "{validation.field.clientCode.notBlank}")
    @Schema(description = "身份域编码")
    private String clientCode;

    @Schema(description = "描述")
    private String remark;

}
