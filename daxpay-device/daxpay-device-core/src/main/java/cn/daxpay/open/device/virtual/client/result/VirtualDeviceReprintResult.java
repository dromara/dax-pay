package cn.daxpay.open.device.virtual.client.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 虚拟设备补打取数结果(平台渲染小票)
@Data
@Accessors(chain = true)
@Schema(title = "虚拟设备补打取数结果")
public class VirtualDeviceReprintResult {

    /// ESC/POS 字节流 Base64(与 pay.success 携带的 printData 同构, 终端直接入打印队列)
    @Schema(description = "ESC/POS 字节流 Base64")
    private String escposBase64;

    /// 80mm 备用流 Base64(双流下发, 终端按本地纸宽设置挑; 台账已 80 或备宽渲染失败时为空)
    @Schema(description = "80mm 备用流 ESC/POS Base64(单流时为空)")
    private String escposBase64W80;

    /// 纸宽(58/80, mm; 终端仅作展示, 不参与哑管道转发)
    @Schema(description = "纸宽(mm)")
    private Integer paperWidth;
}
