package cn.daxpay.open.channel.vbill.param.isv;

import cn.daxpay.open.platform.common.json.deserializer.CredentialKeepWhenBlankDeserializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import tools.jackson.databind.annotation.JsonDeserialize;

/// # 随行付服务商密钥配置保存参数
@Data
@Accessors(chain = true)
@Schema(title = "随行付服务商密钥配置保存参数")
public class VbillIsvKeyConfigParam {

    @NotBlank(message = "{validation.field.product.notBlank}")
    @Schema(description = "产品编码")
    private String product;

    @NotBlank(message = "{validation.field.orgId.notBlank}")
    @Schema(description = "天阙合作机构ID(orgId)")
    private String orgId;

    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @Schema(description = "天阙RSA公钥(X509 Base64, 加密存储)")
    private String publicKey;

    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @Schema(description = "商户RSA私钥(PKCS8 Base64, 加密存储)")
    private String privateKey;

    @NotNull(message = "{validation.field.sandbox.notNull}")
    @Schema(description = "是否沙箱环境")
    private Boolean sandbox;
}
