package cn.daxpay.open.payment.merchant.param.device;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 通道终端报备参数(商户端)
///
/// 报备即绑定: 已绑定的通道终端可不传商户终端编码(沿用已绑定), 更换商户终端须先注销。
/// 入参商户终端编码须为当前商户名下终端, 归属校验由 Service 完成。
@Data
@Accessors(chain = true)
@Schema(title = "通道终端报备参数(商户端)")
public class MchChannelTerminalReportParam {

    @Schema(description = "通道终端主键")
    @NotNull(message = "{validation.field.id.notNull}")
    private Long id;

    /// 商户终端编码(首次报备必填; 已绑定时为空则沿用已绑定终端)
    @Schema(description = "商户终端编码(首次报备必填, 已绑定时可空)")
    private String systemTerminalNo;
}
