package cn.daxpay.open.device.virtual.client.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 虚拟设备补打取数参数(免登录, 设备凭证签名)
///
/// 终端本地待打印队列只存订单元数据, 补打时凭本端点按支付单号实时取平台渲染的
/// ESC/POS 字节流(版式为最新内置版式, 58/80 双流下发由终端按本地纸宽挑)。
/// 签名原料比回执多拼 [bizNo](业务参数防篡改), 其余验签/防重放口径与回执一致。
@Data
@Accessors(chain = true)
@Schema(title = "虚拟设备补打取数参数")
public class VirtualDeviceReprintParam {

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

    /// 签名 HMAC-SHA256(secret, deviceNo + ts + nonce + bizNo) 的十六进制小写
    @NotBlank(message = "{validation.field.sign.notBlank}")
    @Schema(description = "签名(HMAC-SHA256(deviceNo+ts+nonce+bizNo), hex 小写)")
    private String sign;

    /// 支付单号(播报消息中的 bizNo, 补打取数键)
    @NotBlank(message = "{validation.field.bizNo.notBlank}")
    @Schema(description = "支付单号")
    private String bizNo;
}
