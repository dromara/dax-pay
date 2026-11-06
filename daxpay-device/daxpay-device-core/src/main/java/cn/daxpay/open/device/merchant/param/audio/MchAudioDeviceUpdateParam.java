package cn.daxpay.open.device.merchant.param.audio;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 收款设备改名参数(商户端)
///
/// 商户端仅允许改设备名称; 启停走独立端点(change-enable), 归属类字段不可在此修改。
@Data
@Accessors(chain = true)
@Schema(title = "收款设备改名参数(商户端)")
public class MchAudioDeviceUpdateParam {

    /// 设备主键
    @Schema(description = "设备主键")
    @NotNull(message = "{validation.field.id.notNull}")
    private Long id;

    /// 设备名称
    @Schema(description = "设备名称")
    @NotBlank(message = "{validation.field.name.notBlank}")
    @Size(max = 128, message = "{validation.field.name.size}")
    private String name;
}
