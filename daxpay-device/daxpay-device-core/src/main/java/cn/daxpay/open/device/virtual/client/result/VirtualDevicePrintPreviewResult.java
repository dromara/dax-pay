package cn.daxpay.open.device.virtual.client.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 虚拟设备试打取数结果(平台当前版式渲染的样例小票)
///
/// 与推送载荷 `printData` 同格式(ESC/POS Base64 + 纸宽), 终端直接入打印队列;
/// 附带版式代码与名称, 供 APP 在测试打印确认框内提示"本次使用: 标准收款小票"。
@Data
@Accessors(chain = true)
@Schema(title = "虚拟设备试打取数结果")
public class VirtualDevicePrintPreviewResult {

    /// ESC/POS 字节流(Base64, 与 MQTT 推送 printData.escposBase64 同源)
    @Schema(description = "ESC/POS 指令流(Base64)")
    private String escposBase64;

    /// 纸宽(58/80, mm)
    @Schema(description = "纸宽(mm)")
    private Integer paperWidth;

    /// 命中版式代码(内置版式常量)
    @Schema(description = "命中版式代码")
    private String templateCode;

    /// 命中版式名称
    @Schema(description = "命中版式名称")
    private String templateName;
}
