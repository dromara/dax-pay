package cn.daxpay.open.payment.merchant.result.config;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 商户API配置结果
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "商户API配置结果")
public class MerchantCredentialResult extends BaseResult {

    /// 商户公钥（可公开，不脱敏）
    @Schema(description = "商户公钥")
    private String publicKey;

    /// 平台公钥
    @Schema(description = "平台公钥")
    private String platformPublicKey;

    /// 通信密钥
    @Schema(description = "通信密钥")
    @SensitiveInfo
    private String secretKey;
}
