package cn.daxpay.open.device.merchant.param.audio;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 收款设备调整门店参数(商户端)
@Data
@Accessors(chain = true)
@Schema(title = "收款设备调整门店参数(商户端)")
public class MchAudioDeviceBindStoreParam {

    /// 设备主键
    @Schema(description = "设备主键")
    @NotNull(message = "{validation.field.id.notNull}")
    private Long id;

    /// 门店号(须归属当前商户)
    @Schema(description = "门店号")
    @NotBlank(message = "{validation.field.storeNo.notBlank}")
    @Size(max = 50, message = "{validation.field.storeNo.size}")
    private String storeNo;
}
