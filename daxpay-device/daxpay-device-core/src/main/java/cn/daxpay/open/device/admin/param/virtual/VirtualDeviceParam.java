package cn.daxpay.open.device.admin.param.virtual;

import cn.daxpay.open.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 虚拟设备参数
///
/// 运营端仅维护台账事实(名称/启停); 门店属归属类操作, 走绑定商户弹窗(绑定/改绑时一并选定)。
@Data
@Accessors(chain = true)
@Schema(title = "虚拟设备参数")
public class VirtualDeviceParam {

    @Schema(description = "主键")
    @NotNull(message = "{validation.field.id.notNull}", groups = ValidationGroup.edit.class)
    private Long id;

    @Schema(description = "设备名称")
    @NotBlank(message = "{validation.field.name.notBlank}")
    @Size(max = 128, message = "{validation.field.name.size}")
    private String name;

    @Schema(description = "是否启用")
    @NotNull(message = "{validation.field.enabled.notNull}")
    private Boolean enable;
}
