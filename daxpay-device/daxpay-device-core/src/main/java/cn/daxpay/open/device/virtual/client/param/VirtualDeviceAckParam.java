package cn.daxpay.open.device.virtual.client.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 虚拟音箱回执/心跳参数(免登录, 设备凭证签名)
@Data
@Accessors(chain = true)
@Schema(title = "虚拟音箱回执/心跳参数")
public class VirtualDeviceAckParam {

    /// 设备号
    @NotBlank(message = "{validation.field.deviceNo.notBlank}")
    @Schema(description = "设备号")
    private String deviceNo;

    /// 时间戳(epoch millis, 签名原料之一, 超出 ±5 分钟窗口拒收防重放)
    @NotNull(message = "{validation.field.ts.notNull}")
    @Schema(description = "时间戳(epoch millis)")
    private Long ts;

    /// 一次性随机串(签名原料之一, 防重放)
    @NotBlank(message = "{validation.field.nonce.notBlank}")
    @Schema(description = "随机串")
    private String nonce;

    /// 签名 HMAC-SHA256(secret, deviceNo + ts + nonce) 的十六进制小写
    @NotBlank(message = "{validation.field.sign.notBlank}")
    @Schema(description = "签名(HMAC-SHA256(deviceNo+ts+nonce), hex 小写)")
    private String sign;

    /// 关联消息ID(播完回执时携带; 纯心跳不传)
    @Schema(description = "关联消息ID(回执时携带)")
    private String msgId;

    /// 命令名(回执时携带, 如 pay.success)
    @Schema(description = "命令名")
    private String cmd;

    /// 执行是否成功(回执时携带)
    @Schema(description = "执行是否成功")
    private Boolean ok;

    /// 本地凭据版本(激活时下发并持久化; 老版本 APP 不传, 后端跳过比对保持原有行为)
    ///
    /// 版本为空串或 "unknown" 时视为需要刷新(存量设备升级后首次心跳走此分支)。
    @Schema(description = "本地凭据版本(可选, 老客户端不传)")
    private String credentialVersion;
}
