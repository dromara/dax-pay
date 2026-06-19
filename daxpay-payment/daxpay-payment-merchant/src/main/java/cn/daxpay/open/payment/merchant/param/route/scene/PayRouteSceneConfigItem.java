package cn.daxpay.open.payment.merchant.param.route.scene;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 场景模式配置项
@Data
@Accessors(chain = true)
@Schema(title = "场景模式配置项")
public class PayRouteSceneConfigItem {

    @Schema(description = "支付渠道")
    private String provider;

    @Schema(description = "支付产品")
    private String product;

    @Schema(description = "支付能力（场景保存校验用，不落库）")
    private String capability;

    @Schema(description = "支付通道")
    private String channel;

    @Schema(description = "支付方式")
    private String method;
}
