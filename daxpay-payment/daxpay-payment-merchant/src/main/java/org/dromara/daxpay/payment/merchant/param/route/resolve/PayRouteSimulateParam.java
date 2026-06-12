package org.dromara.daxpay.payment.merchant.param.route.resolve;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/// # 通道路由模拟参数
///
@Data
@Accessors(chain = true)
@Schema(title = "通道路由模拟参数")
public class PayRouteSimulateParam {

    @NotBlank(message = "{validation.field.appId.notBlank}")
    @Schema(description = "应用号")
    private String appId;

    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    @Schema(description = "商户号")
    private String mchNo;

    /// 支付渠道（基础/场景模式模拟时必填）
    @Schema(description = "支付渠道: wechat/alipay/union_pay")
    private String provider;

    /// 支付方式（场景模式试算时必填，须为已启用渠道支付方式目录中的 code）
    @Schema(description = "支付方式编码")
    private String method;

    @Schema(description = "支付金额")
    private BigDecimal amount;

    @Schema(description = "模拟使用的路由模式 basic/scene（不传则按策略生效模式；advanced 暂未开放）")
    private String mode;
}
