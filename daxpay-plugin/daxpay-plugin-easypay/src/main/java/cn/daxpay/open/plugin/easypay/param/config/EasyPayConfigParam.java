package cn.daxpay.open.plugin.easypay.param.config;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/// # 易支付场景配置参数
///
@Data
@Schema(title = "易支付配置参数")
public class EasyPayConfigParam {

    /// 主键
    @Schema(description = "主键")
    private Long id;

    /// 易支付商户号
    @NotNull(message = "{validation.field.pid.notNull}")
    @Schema(description = "易支付商户号")
    private Integer pid;

    /// 限制支付
    @Schema(description = "限制支付")
    private String limitPay;
}
