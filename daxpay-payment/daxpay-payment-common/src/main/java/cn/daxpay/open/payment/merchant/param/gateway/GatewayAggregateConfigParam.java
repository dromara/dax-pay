package cn.daxpay.open.payment.merchant.param.gateway;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/// # 网关聚合扫码配置参数
@Data
@Schema(title = "网关聚合扫码配置参数")
public class GatewayAggregateConfigParam {

    @Schema(description = "商户号")
    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    @Size(max = 32, message = "{validation.field.mchNo.size}")
    private String mchNo;

    @Schema(description = "应用号")
    @NotBlank(message = "{validation.field.appId.notBlank}")
    @Size(max = 32, message = "{validation.field.appId.size}")
    private String appId;

    @Schema(description = "微信场景支付产品")
    @Size(max = 32, message = "{validation.field.product.size}")
    private String wxProduct;

    @Schema(description = "微信场景支付方式")
    @Size(max = 32, message = "{validation.field.method.size}")
    private String wxMethod;

    @Schema(description = "支付宝场景支付产品")
    @Size(max = 32, message = "{validation.field.product.size}")
    private String alipayProduct;

    @Schema(description = "支付宝场景支付方式")
    @Size(max = 32, message = "{validation.field.method.size}")
    private String alipayMethod;

    @Schema(description = "云闪付场景支付产品")
    @Size(max = 32, message = "{validation.field.product.size}")
    private String unionProduct;

    @Schema(description = "云闪付场景支付方式")
    @Size(max = 32, message = "{validation.field.method.size}")
    private String unionMethod;

    @Schema(description = "是否自动拉起支付")
    private Boolean autoLaunch;
}
