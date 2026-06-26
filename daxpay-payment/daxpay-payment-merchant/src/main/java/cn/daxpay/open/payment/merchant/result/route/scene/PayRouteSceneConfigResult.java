package cn.daxpay.open.payment.merchant.result.route.scene;

import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 场景模式配置结果
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "场景模式配置结果")
public class PayRouteSceneConfigResult extends BaseResult {

    @Schema(description = "路由策略ID")
    private String strategyId;

    @Schema(description = "支付渠道")
    private String provider;

    @Schema(description = "通道编码")
    private String channel;

    @Schema(description = "支付方式编码")
    private String method;

    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "支付能力编码")
    private String capability;
}
