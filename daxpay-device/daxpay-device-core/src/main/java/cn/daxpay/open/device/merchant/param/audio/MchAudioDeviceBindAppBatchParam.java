package cn.daxpay.open.device.merchant.param.audio;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// # 收款设备批量绑定应用参数(商户端)
///
/// 应用号为空 = 全部应用(清空应用归属); 非空须归属当前商户。
@Data
@Accessors(chain = true)
@Schema(title = "收款设备批量绑定应用参数(商户端)")
public class MchAudioDeviceBindAppBatchParam {

    /// 设备主键集合
    @Schema(description = "设备主键集合")
    @NotEmpty(message = "{validation.field.deviceIds.notEmpty}")
    private List<Long> ids;

    /// 应用号(空=全部应用; 非空须归属当前商户)
    @Schema(description = "应用号(空=全部应用)")
    @Size(max = 50, message = "{validation.field.appId.size}")
    private String appId;
}
