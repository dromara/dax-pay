package org.dromara.daxpay.payment.channel.param.apply;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 商户入驻申请参数
///
@Data
@Accessors(chain = true)
@Schema(title = "商户入驻申请参数")
public class OnbMchApplyParam {

    @Schema(description = "主键")
    private Long id;

    @NotNull(message = "{validation.field.channel.notNull}")
    @Schema(description = "进件通道")
    private String channel;

    /// 进件类型
    @NotNull(message = "{validation.field.applyType.notNull}")
    @Schema(description = "进件类型")
    private String applyType;

    /// 进件商户号
    @Schema(description = "进件商户号")
    private String mchNo;

    /// 进件申请名称
    @NotBlank(message = "{validation.field.name.notBlank}")
    @Schema(description = "进件申请名称名称")
    private String name;

    /// 复用商户资料
    @Schema(description = "复用商户资料")
    private boolean reuseMchProfile;
}
