package cn.daxpay.open.channel.alipay.result.isv;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 支付宝服务商代运营授权链接结果
///
@Data
@Accessors(chain = true)
@Schema(title = "支付宝服务商代运营授权链接结果")
public class AlipayIsvAuthUrlResult {

    /// 支付宝代运营授权深链(可生成二维码供商户扫码)
    @Schema(description = "授权链接")
    private String authUrl;
}
