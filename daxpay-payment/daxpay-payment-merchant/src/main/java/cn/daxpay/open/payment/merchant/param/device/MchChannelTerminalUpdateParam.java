package cn.daxpay.open.payment.merchant.param.device;

import cn.daxpay.open.platform.capability.sensitiveword.validation.SensitiveWord;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 通道终端修改参数(商户端)
///
/// 仅承载名称/备注/报备参数; 通道商户、报送类型与商户终端绑定不在此维护(换绑走报备)。
/// 归属校验由 Service 按主键完成。
@Data
@Accessors(chain = true)
@Schema(title = "通道终端修改参数(商户端)")
public class MchChannelTerminalUpdateParam {

    @Schema(description = "主键")
    @NotNull(message = "{validation.field.id.notNull}")
    private Long id;

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
