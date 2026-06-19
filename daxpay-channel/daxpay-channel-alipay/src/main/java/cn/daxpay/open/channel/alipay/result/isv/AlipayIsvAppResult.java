package cn.daxpay.open.channel.alipay.result.isv;

import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付宝服务商应用
///
/// 支付宝服务商应用的返回结果对象，包含应用名称和支付宝应用ID等信息。
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
