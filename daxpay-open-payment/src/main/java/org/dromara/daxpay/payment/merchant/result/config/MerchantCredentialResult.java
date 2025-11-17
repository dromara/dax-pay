package org.dromara.daxpay.payment.merchant.result.config;

import cn.bootx.platform.common.jackson.sensitive.SensitiveInfo;
import cn.bootx.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 商户API配置结果
 * @author xxm
 * @since 2025/9/13
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "商户API配置结果")
public class MerchantCredentialResult extends BaseResult {

    /** 商户公钥 */
    @Schema(description = "商户公钥")
    @SensitiveInfo(front = 12, end = 12)
    private String publicKey;

    /** 平台公钥 */
    @Schema(description = "平台公钥")
    private String platformPublicKey;

    /** 通信密钥 */
    @Schema(description = "通信密钥")
    @SensitiveInfo
    private String secretKey;
}
