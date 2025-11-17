package org.dromara.daxpay.payment.merchant.param.config;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 商户API配置参数
 * @author xxm
 * @since 2025/9/13
 */
@Data
@Accessors(chain = true)
@Schema(title = "商户API配置参数")
public class MerchantCredentialParam {

    /** 服务商号 */
    @Schema(description = "服务商号")
    private String isvNo;

    /** 商户号 */
    @Schema(description = "商户号")
    @NotNull(message = "商户号不可为空")
    private String mchNo;

    /** 商户公钥 */
    @Schema(description = "商户公钥")
    private String publicKey;

    /** 通信密钥 */
    @Schema(description = "通信密钥")
    private String secretKey;
}
