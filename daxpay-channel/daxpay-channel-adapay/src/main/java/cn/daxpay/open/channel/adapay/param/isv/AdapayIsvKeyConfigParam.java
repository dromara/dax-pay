package cn.daxpay.open.channel.adapay.param.isv;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # Adapay 服务商密钥配置保存参数
@Data
@Accessors(chain = true)
@Schema(title = "Adapay 服务商密钥配置保存参数")
public class AdapayIsvKeyConfigParam {

    @NotBlank(message = "{validation.field.isvNo.notBlank}")
    @Schema(description = "服务商号(平台在汇付的服务商/主体编号)")
    private String isvNo;

    @Schema(description = "Adapay 交易密钥(加密存储)")
    private String apiKey;

    @Schema(description = "商户RSA私钥(PKCS#8 Base64, 加密存储)")
    private String privateKey;

    @Schema(description = "Adapay 平台公钥(X509 Base64, 加密存储; 为空使用全局默认)")
    private String publicKey;

    @NotNull(message = "{validation.field.sandbox.notNull}")
    @Schema(description = "是否沙箱环境")
    private Boolean sandbox;
}
