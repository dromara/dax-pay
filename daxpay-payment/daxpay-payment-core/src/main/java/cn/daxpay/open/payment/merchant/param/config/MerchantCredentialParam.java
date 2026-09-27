package cn.daxpay.open.payment.merchant.param.config;

import cn.daxpay.open.platform.common.json.deserializer.CredentialKeepWhenBlankDeserializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import tools.jackson.databind.annotation.JsonDeserialize;

/// # 商户API配置参数
///
@Data
@Accessors(chain = true)
@Schema(title = "商户API配置参数")
public class MerchantCredentialParam {

    /// 商户号
    @Schema(description = "商户号")
    @NotNull(message = "{validation.field.mchNo.notNull}")
    private String mchNo;

    /// 商户公钥
    @Schema(description = "商户公钥")
    private String publicKey;

    /// 通信密钥
    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @Schema(description = "通信密钥")
    private String secretKey;
}
