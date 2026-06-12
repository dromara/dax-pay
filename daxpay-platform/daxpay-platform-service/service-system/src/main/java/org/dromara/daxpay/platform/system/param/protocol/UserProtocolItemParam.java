package org.dromara.daxpay.platform.system.param.protocol;

import org.dromara.daxpay.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 用户协议项管理
///
@Data
@Accessors(chain = true)
@Schema(title = "用户协议项管理")
public class UserProtocolItemParam {

    @Null(message = "{validation.field.id.mustBeNullOnAdd}", groups = ValidationGroup.add.class)
    @NotNull(message = "{validation.field.id.notNull}", groups = ValidationGroup.edit.class)
    @Schema(description = "主键")
    private Long id;

    /// 协议id
    @NotNull(message = "{validation.field.protocolId.notNull}")
    @Schema(description = "协议id")
    private Long protocolId;

    /// 菜单排序
    @Schema(description = "菜单排序")
    private Double sortNo;

    /// 协议内容
    @NotBlank(message = "{validation.field.content.notBlank}")
    @Schema(description = "协议内容")
    private String content;
}
