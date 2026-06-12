package org.dromara.daxpay.payment.merchant.param.route.scene;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 场景模式支付能力批量候选项
@Data
@Accessors(chain = true)
@Schema(title = "场景模式支付能力批量候选项")
public class PayRouteSceneCapabilityBatchItem {

    @NotBlank(message = "{validation.field.provider.notBlank}")
    @Schema(description = "支付渠道")
    private String provider;

    @NotBlank(message = "{validation.field.method.notBlank}")
    @Schema(description = "支付方式")
    private String method;

    @NotBlank(message = "{validation.field.product.notBlank}")
    @Schema(description = "支付产品")
    private String product;
}