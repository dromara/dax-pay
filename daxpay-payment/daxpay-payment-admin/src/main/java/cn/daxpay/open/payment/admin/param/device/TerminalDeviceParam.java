package cn.daxpay.open.payment.admin.param.device;

import cn.daxpay.open.platform.capability.sensitiveword.validation.SensitiveWord;
import cn.daxpay.open.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 系统终端参数
@Data
@Accessors(chain = true)
@Schema(title = "系统终端参数")
public class TerminalDeviceParam {

    @Schema(description = "主键")
    @NotNull(message = "{validation.field.id.notNull}", groups = ValidationGroup.edit.class)
    private Long id;

    @Schema(description = "商户号(新增必填)")
    @NotBlank(message = "{validation.field.mchNo.notBlank}", groups = ValidationGroup.add.class)
    private String mchNo;

    @Schema(description = "终端名称")
    @NotBlank(message = "{validation.field.name.notBlank}")
    @Size(max = 100, message = "{validation.field.deviceName.size}")
    @SensitiveWord
    private String name;

    @Schema(description = "门店号(可空)")
    @Size(max = 64, message = "{validation.field.storeNo.size}")
    private String storeNo;

    @Schema(description = "是否启用")
    @NotNull(message = "{validation.field.enable.notNull}")
    private Boolean enable;

    @Schema(description = "备注")
    @Size(max = 255, message = "{validation.field.remark.size}")
    private String remark;
}
