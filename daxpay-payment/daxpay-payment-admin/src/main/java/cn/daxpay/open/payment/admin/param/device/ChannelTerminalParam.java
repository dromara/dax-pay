package cn.daxpay.open.payment.admin.param.device;

import cn.daxpay.open.payment.device.enums.TerminalTypeEnum;
import cn.daxpay.open.platform.capability.sensitiveword.validation.SensitiveWord;
import cn.daxpay.open.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 通道终端台账参数
///
/// 新增建档即绑定商户终端; 通道侧终端号与登记状态由手动登记
/// ([ChannelTerminalRegisterParam]) 动作回写, 不在本参数维护。
@Data
@Accessors(chain = true)
@Schema(title = "通道终端台账参数")
public class ChannelTerminalParam {

    @Schema(description = "主键")
    @NotNull(message = "{validation.field.id.notNull}", groups = ValidationGroup.edit.class)
    private Long id;

    @Schema(description = "商户号(新增必填)")
    @NotBlank(message = "{validation.field.mchNo.notBlank}", groups = ValidationGroup.add.class)
    private String mchNo;

    @Schema(description = "通道商户号(新增必填)")
    @NotBlank(message = "{validation.field.channelMchNo.notBlank}", groups = ValidationGroup.add.class)
    private String channelMchNo;

    /// 报送类型
    /// @see TerminalTypeEnum
    @Schema(description = "报送类型(新增必填)")
    @NotBlank(message = "{validation.field.type.notBlank}", groups = ValidationGroup.add.class)
    private String type;

    /// 绑定的商户终端编码(建档即绑定, 新增必填)
    @Schema(description = "绑定的商户终端编码(新增必填)")
    @NotBlank(message = "{validation.field.systemTerminalNo.notBlank}", groups = ValidationGroup.add.class)
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
