package org.dromara.daxpay.payment.merchant.result.route.basic;

import org.dromara.daxpay.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/// # 支付通道路由基础模式配置结果
///
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Schema(title = "支付通道路由基础模式配置结果")
public class PayRouteBasicConfigResult extends BaseResult {

    /// 支付渠道
    @Schema(description = "支付渠道: wechat/alipay/union_pay")
    private String provider;

    @Schema(description = "支付产品编码")
    private String product;

    /// 该渠道下可选支付产品编码列表
    @Schema(description = "可选支付产品编码")
    private List<String> products = new ArrayList<>();
}
