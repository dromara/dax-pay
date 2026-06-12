package org.dromara.daxpay.payment.merchant.param.route.scene;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// # 场景模式支付能力批量候选参数
@Data
@Accessors(chain = true)
@Schema(title = "场景模式支付能力批量候选参数")
public class PayRouteSceneCapabilityBatchParam {

    @NotBlank(message = "{validation.field.appId.notBlank}")
    @Schema(description = "应用号")
    private String appId;

    @Valid
    @Schema(description = "目录项与产品列表（仅含已选产品的行）")
    private List<PayRouteSceneCapabilityBatchItem> items;
}