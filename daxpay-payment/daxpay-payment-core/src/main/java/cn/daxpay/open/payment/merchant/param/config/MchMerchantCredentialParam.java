package cn.daxpay.open.payment.merchant.param.config;

import cn.daxpay.open.platform.common.json.deserializer.CredentialKeepWhenBlankDeserializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import tools.jackson.databind.annotation.JsonDeserialize;

/// # 商户API对接配置更新参数(商户端)
///
/// 商户端专用参数, 不含商户号(防越权: 由控制器从 [PaymentContext] 取当前登录商户组装,
/// 对照 [MerchantCredentialParam])。
@Data
@Accessors(chain = true)
@Schema(title = "商户API对接配置更新参数(商户端)")
public class MchMerchantCredentialParam {

    /// 商户公钥
    @Schema(description = "商户公钥")
    private String publicKey;

    /// 通信密钥
    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @Schema(description = "通信密钥")
    private String secretKey;
}
