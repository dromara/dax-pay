package cn.daxpay.open.device.merchant.param.audio;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 收款设备调整应用参数(商户端)
@Data
@Accessors(chain = true)
@Schema(title = "收款设备调整应用参数(商户端)")
public class MchAudioDeviceBindAppParam {

    /// 设备主键
    @Schema(description = "设备主键")
    @NotNull(message = "{validation.field.id.notNull}")
    private Long id;

    /// 应用号(空=全部应用; 非空须归属当前商户)
    @Schema(description = "应用号(空=全部应用)")
    @Size(max = 50, message = "{validation.field.appId.size}")
    private String appId;
}
