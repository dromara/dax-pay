package cn.daxpay.open.payment.merchant.param.route.basic;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 支付通道路由基础模式配置项
///
@Data
@Accessors(chain = true)
@Schema(title = "支付通道路由基础模式配置项")
public class PayRouteBasicConfigItem {

    @NotBlank(message = "{validation.field.provider.notBlank}")
    @Schema(description = "支付渠道: wechat/alipay/union_pay")
    private String provider;

    @Schema(description = "通道商户号，空表示清除该渠道配置")
    private String channelMchNo;
}
