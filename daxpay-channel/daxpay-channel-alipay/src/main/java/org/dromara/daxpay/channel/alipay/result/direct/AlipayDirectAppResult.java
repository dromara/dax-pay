package org.dromara.daxpay.channel.alipay.result.direct;

import org.dromara.daxpay.payment.common.result.MchBaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付宝直连商户应用
///
/// 支付宝直连商户应用的返回结果对象，包含通道商户号、应用名称和支付宝应用ID等信息。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付宝直连商户应用")
public class AlipayDirectAppResult extends MchBaseResult {

    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "应用名称")
    private String appName;

    @Schema(description = "支付宝应用ID")
    private String aliAppId;
}
