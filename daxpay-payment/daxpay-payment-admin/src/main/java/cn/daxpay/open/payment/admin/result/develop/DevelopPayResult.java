package cn.daxpay.open.payment.admin.result.develop;

import cn.daxpay.open.payment.unipay.result.trade.pay.PayResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 支付调试结果
@Data
@Accessors(chain = true)
@Schema(title = "支付调试结果")
public class DevelopPayResult {

    /// 发送的请求体 JSON
    @Schema(description = "请求体JSON")
    private String requestBody;

    /// 签名信息(传入私钥时返回)
    @Schema(description = "签名信息")
    private DevelopSignResult signInfo;

    /// 支付结果
    @Schema(description = "支付结果")
    private PayResult payResult;
}
