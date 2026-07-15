package cn.daxpay.open.plugin.risk.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 风险命中处理参数
///
@Data
@Accessors(chain = true)
@Schema(title = "风险命中处理参数")
public class PayRiskHitHandleParam {

    @Schema(description = "主键")
    @NotNull(message = "{validation.field.id.notNull}")
    private Long id;

    /// 处理状态：ignored / added_blacklist / merchant_disabled / other
    @Schema(description = "处理状态")
    @NotBlank(message = "{validation.field.status.notBlank}")
    @Size(max = 32, message = "{validation.field.product.size}")
    private String handleStatus;

    @Schema(description = "处理说明")
    @Size(max = 500, message = "{validation.field.remark.size}")
    private String handleRemark;
}
