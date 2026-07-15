package cn.daxpay.open.payment.merchant.result.gateway;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// # 码牌支付策略配置结果
@Data
@Accessors(chain = true)
@Schema(title = "码牌支付策略配置结果")
public class GatewayCodeConfigResult {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "商户号")
    private String mchNo;

    @Schema(description = "应用号")
    private String appId;

    /// @see cn.daxpay.open.payment.merchant.enums.AggregateConfigLevelEnum
    @Schema(description = "配置深度: auto/method/direct")
    private String level;

    @Schema(description = "客户端环境×形态配置列表")
    private List<GatewayCodeClientEnvResult> clientEnvs;
}
