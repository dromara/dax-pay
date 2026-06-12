package org.dromara.daxpay.payment.channel.param.apply;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 进件商户申请审核参数
///
@Data
@Accessors(chain = true)
@Schema(title = "进件商户申请审核参数")
public class OnbMchApplyAuditParam {
    @NotNull(message = "{validation.field.id.notNull}")
    @Schema(description = "主键")
    private Long id;

    @NotNull(message = "{validation.field.pass.notNull}")
    @Schema(description = "审核结果")
    private Boolean pass;

    @Schema(description = "驳回内容")
    private String rejectReason;

}
