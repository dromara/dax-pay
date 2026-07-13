package cn.daxpay.open.payment.route.result.basic;

import cn.daxpay.open.platform.core.result.BaseResult;
import cn.daxpay.open.platform.core.rest.dto.LabelValue;
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

    @Schema(description = "通道商户号")
    private String channelMchNo;

    /// 该渠道下可选通道商户列表(名称/号码)
    @Schema(description = "可选通道商户列表")
    private List<LabelValue> channelMchants = new ArrayList<>();
}
