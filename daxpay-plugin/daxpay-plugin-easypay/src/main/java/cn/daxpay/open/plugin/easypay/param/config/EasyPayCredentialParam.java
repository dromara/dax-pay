package cn.daxpay.open.plugin.easypay.param.config;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(title = "易支付凭证参数")
public class EasyPayCredentialParam {

    @Schema(description = "主键")
    private Long id;

    @NotBlank(message = "{validation.field.appId.notBlank}")
    @Schema(description = "应用号")
    private String appId;

    @Schema(description = "启用")
    private Boolean enable;

    @Schema(description = "开启V1")
    private Boolean enableV1;

    @Schema(description = "开启V2")
    private Boolean enableV2;

    @Schema(description = "MD5密钥")
    private String md5Key;

    @Schema(description = "使用系统密钥")
    private Boolean useSystemKey;

    @Schema(description = "商户RSA公钥")
    private String publicKey;
}
