package cn.daxpay.open.payment.admin.result.develop;

import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPayResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 支付调试结果
@Data
@Accessors(chain = true)
@Schema(title = "支付调试结果")
public class DevelopPayResult {

    /// 支付结果
    @Schema(description = "支付结果")
    private NormalPayResult payResult;

    /// 响应签名(平台私钥签名, 可用于验证响应完整性)
    @Schema(description = "响应签名")
    private String sign;
}
