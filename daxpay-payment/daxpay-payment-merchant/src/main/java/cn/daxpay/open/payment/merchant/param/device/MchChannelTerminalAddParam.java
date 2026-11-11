package cn.daxpay.open.payment.merchant.param.device;

import cn.daxpay.open.payment.device.enums.TerminalTypeEnum;
import cn.daxpay.open.platform.capability.sensitiveword.validation.SensitiveWord;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 通道终端新增参数(商户端)
///
/// 不含商户号: 归属商户强制取自 [cn.daxpay.open.payment.common.context.PaymentContext],
/// 通道商户号由抽屉上下文带出并校验归属当前商户。建档即绑定所选商户终端(须为当前商户名下)。
@Data
@Accessors(chain = true)
@Schema(title = "通道终端新增参数(商户端)")
public class MchChannelTerminalAddParam {

    @Schema(description = "通道商户号")
    @NotBlank(message = "{validation.field.channelMchNo.notBlank}")
    private String channelMchNo;

    /// 报送类型
    /// @see TerminalTypeEnum
    @Schema(description = "报送类型")
    @NotBlank(message = "{validation.field.type.notBlank}")
    private String type;

    /// 绑定的商户终端编码(须为当前商户名下终端)
    @Schema(description = "绑定的商户终端编码")
    @NotBlank(message = "{validation.field.systemTerminalNo.notBlank}")
    private String systemTerminalNo;

    @Schema(description = "终端名称")
    @NotBlank(message = "{validation.field.name.notBlank}")
    @Size(max = 100, message = "{validation.field.deviceName.size}")
    @SensitiveWord
    private String name;

    @Schema(description = "通道差异化报备参数(JSON)")
    private String extra;

    @Schema(description = "备注")
    @Size(max = 255, message = "{validation.field.remark.size}")
    private String remark;
}
