package org.dromara.daxpay.channel.alipay.result.app;

import org.dromara.daxpay.payment.common.result.MchBaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付宝直连商户应用
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付宝直连商户应用")
public class AlipayMchAppResult extends MchBaseResult {

    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "应用名称")
    private String appName;

    @Schema(description = "支付宝应用ID")
    private String aliAppId;
}
