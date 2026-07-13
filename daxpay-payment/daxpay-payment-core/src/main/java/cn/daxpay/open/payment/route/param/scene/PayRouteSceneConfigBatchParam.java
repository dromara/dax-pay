package cn.daxpay.open.payment.route.param.scene;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// # 支付通道路由场景模式配置批量保存参数
///
@Data
@Accessors(chain = true)
@Schema(title = "支付通道路由场景模式配置批量保存参数")
public class PayRouteSceneConfigBatchParam {

    @NotBlank(message = "{validation.field.appId.notBlank}")
    @Schema(description = "应用号")
    private String appId;

    @NotEmpty(message = "{validation.field.configItems.notEmpty}")
    @Valid
    @Schema(description = "配置项列表")
    private List<PayRouteSceneConfigItem> items;
}
