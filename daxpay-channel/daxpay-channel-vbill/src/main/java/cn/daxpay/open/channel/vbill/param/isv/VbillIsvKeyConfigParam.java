package cn.daxpay.open.channel.vbill.param.isv;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

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

    @Schema(description = "天阙RSA公钥(X509 Base64, 加密存储)")
    private String publicKey;

    @Schema(description = "商户RSA私钥(PKCS8 Base64, 加密存储)")
    private String privateKey;

    @Schema(description = "是否沙箱环境")
    private Boolean sandbox;
}
