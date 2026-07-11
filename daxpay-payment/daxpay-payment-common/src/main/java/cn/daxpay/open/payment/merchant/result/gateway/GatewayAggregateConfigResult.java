package cn.daxpay.open.payment.merchant.result.gateway;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 网关聚合扫码配置结果
@Data
@Accessors(chain = true)
@Schema(title = "网关聚合扫码配置结果")
public class GatewayAggregateConfigResult {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "商户号")
    private String mchNo;

    @Schema(description = "应用号")
    private String appId;

    @Schema(description = "微信场景支付产品")
    private String wxProduct;

    @Schema(description = "微信场景支付方式")
    private String wxMethod;

    @Schema(description = "支付宝场景支付产品")
    private String alipayProduct;

    @Schema(description = "支付宝场景支付方式")
    private String alipayMethod;

    @Schema(description = "云闪付场景支付产品")
    private String unionProduct;

    @Schema(description = "云闪付场景支付方式")
    private String unionMethod;

    @Schema(description = "是否自动拉起支付")
    private Boolean autoLaunch;
}
