package org.dromara.daxpay.channel.alipay.result.app;

import org.dromara.daxpay.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付宝服务商应用
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付宝服务商应用")
public class AlipayIsvAppResult extends BaseResult {

    /// 应用名称
    @Schema(description = "应用名称")
    private String appName;

    /// 支付宝应用ID
    @Schema(description = "支付宝应用ID")
    private String aliAppId;
}
