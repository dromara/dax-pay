package cn.daxpay.open.plugin.easypay.result.config;

import cn.daxpay.open.payment.common.result.MchBaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 易支付场景配置结果
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "易支付配置结果")
public class EasyPayConfigResult extends MchBaseResult {

    /// 易支付商户号
    @Schema(description = "易支付商户号")
    private Integer pid;

    /// 应用号
    @Schema(description = "应用号")
    private String appId;

    /// 限制支付
    @Schema(description = "限制支付")
    private String limitPay;
}
