package cn.daxpay.open.payment.device.terminal.param;

import cn.daxpay.open.payment.device.enums.TerminalTypeEnum;
import cn.daxpay.open.platform.capability.sensitiveword.validation.SensitiveWord;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 通道终端建档/修改参数(多端共用)
///
/// 不承载商户号: 归属商户由调用方显式传入(运营端取业务参数, 商户端取 [cn.daxpay.open.payment.common.context.PaymentContext]),
/// 避免请求体伪造目标商户。必填项按调用场景校验:
/// - 新增: channelMchNo / type / systemTerminalNo 必填;
/// - 修改: id 必填(仅更新名称/备注/报备参数, 通道商户与报送类型不改)。
@Data
@Accessors(chain = true)
@Schema(title = "通道终端建档/修改参数")
public class ChannelTerminalSaveParam {

    @Schema(description = "主键(修改必填)")
    private Long id;

    @Schema(description = "通道商户号(新增必填)")
    private String channelMchNo;

    /// 报送类型
    /// @see TerminalTypeEnum
    @Schema(description = "报送类型(新增必填)")
    private String type;

    /// 绑定的系统终端编码(建档即绑定, 新增必填)
    @Schema(description = "绑定的系统终端编码(新增必填)")
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
