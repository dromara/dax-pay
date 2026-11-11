package cn.daxpay.open.payment.device.terminal.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 通道终端报备参数(多端共用)
///
/// 报备即绑定: 选择商户终端后同步建立关联; 已绑定的通道终端重新报备可不传(沿用已绑定终端),
/// 更换商户终端须先注销(通道侧设备标识已上交, 换终端等于换设备)。不承载商户号, 归属校验由调用方完成。
@Data
@Accessors(chain = true)
@Schema(title = "通道终端报备参数")
public class ChannelTerminalReportParam {

    @Schema(description = "通道终端主键")
    @NotNull(message = "{validation.field.id.notNull}")
    private Long id;

    /// 商户终端编码(首次报备必填; 已绑定时为空则沿用已绑定终端)
    @Schema(description = "商户终端编码(首次报备必填, 已绑定时可空)")
    private String systemTerminalNo;
}
