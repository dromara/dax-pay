package cn.daxpay.open.payment.device.printer.param;

import cn.daxpay.open.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 云打印设备
@Data
@Accessors(chain = true)
@Schema(title = "云打印设备")
public class DevicePrinterParam {

    /// 主键
    @Schema(description = "主键")
    @NotNull(message = "{validation.field.id.notNull}", groups = ValidationGroup.edit.class)
    private Long id;

    /// 商户号(划拨后设置, 初始为空)
    @Schema(description = "商户号")
    private String mchNo;

    /// 厂商代码
    @Schema(description = "厂商代码")
    @NotBlank(message = "{validation.field.vendorCode.notBlank}")
    private String vendorCode;

    /// 厂商配置ID
    @Schema(description = "厂商配置ID")
    @NotNull(message = "{validation.field.vendorConfigId.notNull}")
    private Long vendorConfigId;

    /// 设备序列号
    @Schema(description = "设备序列号")
    @NotBlank(message = "{validation.field.deviceSn.notBlank}")
    private String deviceSn;

    /// 设备IMEI
    @Schema(description = "设备IMEI")
    private String imei;

    /// 厂商门店ID
    @Schema(description = "厂商门店ID")
    private String shopId;

    /// 设备名称
    @Schema(description = "设备名称")
    private String deviceName;

    /// 备注
    @Schema(description = "备注")
    private String remark;
}
