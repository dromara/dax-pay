package cn.daxpay.open.device.virtual.client.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 虚拟音箱回执/心跳结果(设备状态校准)
@Data
@Accessors(chain = true)
@Schema(title = "虚拟音箱回执/心跳结果")
public class VirtualDeviceAckResult {

    /// 凭证是否仍有效(false=删除/换密/验签失败, 终端应清凭证回激活页)
    @Schema(description = "凭证是否仍有效")
    private Boolean valid;

    /// 设备状态(见 DeviceClientStatusEnum)
    @Schema(description = "设备状态(active/disabled/unbound/revoked)")
    private String status;

    /// 设备是否启用(停用时终端应停止播报)
    @Schema(description = "是否启用")
    private Boolean enable;

    /// 是否仍绑定商户(解绑后终端应断开订阅并提示)
    @Schema(description = "是否仍绑定商户")
    private Boolean bound;

    /// 平台当前凭据版本(终端据此刷新本地值; 老客户端忽略)
    @Schema(description = "平台当前凭据版本")
    private String credentialVersion;

    /// 是否需要重新激活(true=本地凭据版本已过期, 终端应重新激活取新凭据)
    ///
    /// 与 [valid] 语义区分: valid=false 是凭证作废(删除/解绑/验签失败, 应清凭证);
    /// needReactivate=true 仅表示 MQTT 接入凭据已轮换, 播报密钥仍有效, 刷新即可恢复。
    @Schema(description = "是否需要重新激活")
    private Boolean needReactivate;
}
