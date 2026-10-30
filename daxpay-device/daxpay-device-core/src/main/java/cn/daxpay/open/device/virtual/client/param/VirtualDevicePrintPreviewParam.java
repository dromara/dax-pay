package cn.daxpay.open.device.virtual.client.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 虚拟设备试打取数参数(免登录, 设备凭证签名)
///
/// 终端"测试打印"不用本地硬编码版式, 而是凭本端点向平台取**当前生效版式**渲染的
/// ESC/POS 字节流, 打完与线上真实收款小票一致(版式链为平台权威, 纸宽默认 58mm)。
/// 签名原料与回执一致(deviceNo + ts + nonce + paperWidth), 纸宽参与签名防改宽重放。
@Data
@Accessors(chain = true)
@Schema(title = "虚拟设备试打取数参数")
public class VirtualDevicePrintPreviewParam {

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

    /// 签名 HMAC-SHA256(secret, deviceNo + ts + nonce + paperWidth) 的十六进制小写
    @NotBlank(message = "{validation.field.sign.notBlank}")
    @Schema(description = "签名(HMAC-SHA256(deviceNo+ts+nonce+paperWidth), hex 小写)")
    private String sign;

    /// 本次渲染纸宽(58/80); 0 或空=58mm(参与签名, 防改宽重放)
    @Schema(description = "渲染纸宽(58/80), 0=58mm")
    private Integer paperWidth;
}
