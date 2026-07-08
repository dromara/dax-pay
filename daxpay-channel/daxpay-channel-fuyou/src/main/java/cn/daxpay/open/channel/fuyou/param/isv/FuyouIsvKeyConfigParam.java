package cn.daxpay.open.channel.fuyou.param.isv;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 富友服务商密钥配置保存参数
@Data
@Accessors(chain = true)
@Schema(title = "富友服务商密钥配置保存参数")
public class FuyouIsvKeyConfigParam {

    @NotBlank(message = "{validation.field.product.notBlank}")
    @Schema(description = "产品编码")
    private String product;

    @NotBlank(message = "{validation.field.fyAppId.notBlank}")
    @Schema(description = "富友应用编号(机构号 ins_cd)")
    private String fyAppId;

    @Schema(description = "富友订单前缀(关联订单号前缀)")
    private String orderPrefix;

    @Schema(description = "商户RSA私钥(PKCS8 Base64, 加密存储)")
    private String privateKey;

    @Schema(description = "富友RSA公钥(X509 Base64, 加密存储)")
    private String publicKey;

    @NotNull(message = "{validation.field.sandbox.notNull}")
    @Schema(description = "是否沙箱环境")
    private Boolean sandbox;
}
