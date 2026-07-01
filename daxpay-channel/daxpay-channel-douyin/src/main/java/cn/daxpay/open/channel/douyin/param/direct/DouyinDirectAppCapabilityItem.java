package cn.daxpay.open.channel.douyin.param.direct;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 抖音直连支付能力关联应用单项
///
/// 用于批量保存时，声明单个支付能力绑定的直连应用。
///
@Data
@Accessors(chain = true)
@Schema(title = "抖音直连支付能力关联应用单项")
public class DouyinDirectAppCapabilityItem {

    @NotBlank(message = "{validation.field.capability.notBlank}")
    @Schema(description = "支付能力编码")
    private String capability;

    @NotNull(message = "{validation.field.douyinDirectAppId.notNull}")
    @Schema(description = "关联抖音直连应用ID")
    private Long douyinDirectAppId;
}
