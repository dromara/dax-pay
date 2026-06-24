package cn.daxpay.open.platform.system.param.protocol;

import cn.daxpay.open.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 用户协议管理
///
@Data
@Accessors(chain = true)
@Schema(title = "用户协议管理")
public class UserProtocolParam {

    @Null(message = "{validation.field.id.mustBeNullOnAdd}", groups = ValidationGroup.add.class)
    @NotNull(message = "{validation.field.id.notNull}", groups = ValidationGroup.edit.class)
    @Schema(description = "主键")
    private Long id;

    /// 名称
    @NotBlank(message = "{validation.field.name.notBlank}")
    @Schema(description = "名称")
    private String name;

    /// 显示名称
    @NotBlank(message = "{validation.field.showName.notBlank}")
    @Schema(description = "显示名称")
    private String showName;

    /// 类型
    @NotBlank(message = "{validation.field.type.notBlank}")
    @Schema(description = "类型")
    private String type;

    /// 端类型
    @NotBlank(message = "{validation.field.clientType.notBlank}")
    @Schema(description = "端类型")
    private String clientType;

    /// 默认语言
    @NotBlank(message = "{validation.field.language.notBlank}")
    @Schema(description = "默认语言")
    private String defaultLanguage;
}

