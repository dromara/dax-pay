package cn.daxpay.open.payment.iot.speaker.param;

import cn.daxpay.open.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 云音响设备
@Data
@Accessors(chain = true)
@Schema(title = "云音响设备")
public class IotSpeakerDeviceParam {

    /// 主键
    @Schema(description = "主键")
    @NotNull(message = "{validation.field.id.notNull}", groups = ValidationGroup.edit.class)
    private Long id;

    /// 商户号
    @Schema(description = "商户号")
    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    private String mchNo;

    /// 设备序列号
    @Schema(description = "设备序列号")
    @NotBlank(message = "{validation.field.deviceSn.notBlank}")
    private String deviceSn;

    /// 设备IMEI
    @Schema(description = "设备IMEI")
    private String imei;

    /// 商米门店ID
    @Schema(description = "商米门店ID")
    private String shopId;

    /// 设备名称
    @Schema(description = "设备名称")
    private String deviceName;

    /// 备注
    @Schema(description = "备注")
    private String remark;
}
