package cn.daxpay.open.channel.hmpay.param.isv;

import cn.daxpay.open.platform.common.json.deserializer.CredentialKeepWhenBlankDeserializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import tools.jackson.databind.annotation.JsonDeserialize;

/// # 河马付服务商密钥配置保存参数
@Data
@Accessors(chain = true)
@Schema(title = "河马付服务商密钥配置保存参数")
public class HmpayIsvKeyConfigParam {

    @NotBlank(message = "{validation.field.product.notBlank}")
    @Schema(description = "产品编码")
    private String product;

    @NotBlank(message = "{validation.field.sandAppId.notBlank}")
    @Schema(description = "杉德代理号(sandAppId)")
    private String sandAppId;

    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @Schema(description = "商户RSA私钥(PKCS#8 Base64, 加密存储)")
    private String privateKey;

    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @Schema(description = "杉德RSA公钥(X509 Base64, 加密存储)")
    private String publicKey;

    @NotNull(message = "{validation.field.sandbox.notNull}")
    @Schema(description = "是否沙箱环境")
    private Boolean sandbox;
}
