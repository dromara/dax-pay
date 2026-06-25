package cn.daxpay.open.payment.device.vendor.param;

import cn.daxpay.open.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 设备厂商配置
@Data
@Accessors(chain = true)
@Schema(title = "设备厂商配置")
public class DeviceVendorConfigParam {

    /// 主键
    @Schema(description = "主键")
    @NotNull(message = "{validation.field.id.notNull}", groups = ValidationGroup.edit.class)
    private Long id;

    /// 设备类型
    @Schema(description = "设备类型")
    @NotBlank(message = "{validation.field.deviceType.notBlank}")
    private String deviceType;

    /// 厂商代码
    @Schema(description = "厂商代码")
    @NotBlank(message = "{validation.field.vendorCode.notBlank}")
    private String vendorCode;

    /// 配置名称
    @Schema(description = "配置名称")
    @NotBlank(message = "{validation.field.configName.notBlank}")
    private String configName;

    /// 厂商应用ID
    @Schema(description = "厂商应用ID")
    private String appId;

    /// 厂商应用密钥
    @Schema(description = "厂商应用密钥")
    private String appSecret;

    /// 是否启用
    @Schema(description = "是否启用")
    private boolean enable;

    /// 扩展参数(JSON)
    @Schema(description = "扩展参数(JSON)")
    private String extParam;

    /// 备注
    @Schema(description = "备注")
    private String remark;
}
