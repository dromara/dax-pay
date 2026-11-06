package cn.daxpay.open.device.merchant.param.audio;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// # 收款设备批量绑定门店参数(商户端)
///
/// 门店号为空 = 解绑(置为未分配, 不参与门店级播报/打印匹配); 非空须归属当前商户。
@Data
@Accessors(chain = true)
@Schema(title = "收款设备批量绑定门店参数(商户端)")
public class MchAudioDeviceBindStoreBatchParam {

    /// 设备主键集合
    @Schema(description = "设备主键集合")
    @NotEmpty(message = "{validation.field.deviceIds.notEmpty}")
    private List<Long> ids;

    /// 门店号(空=解绑/未分配)
    @Schema(description = "门店号(空=解绑/未分配)")
    private String storeNo;
}
