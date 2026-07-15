package cn.daxpay.open.plugin.easypay.param.config;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/// # 易支付凭证参数
///
@Data
@Schema(title = "易支付凭证参数")
public class EasyPayCredentialParam {

    /// 主键
    @Schema(description = "主键")
    private Long id;

    /// 应用号
    @NotBlank(message = "{validation.field.appId.notBlank}")
    @Schema(description = "应用号")
    private String appId;

    /// 启用
    @Schema(description = "启用")
    private Boolean enable;

    /// 开启 V1
    @Schema(description = "开启V1")
    private Boolean enableV1;

    /// 开启 V2
    @Schema(description = "开启V2")
    private Boolean enableV2;

    /// V1 MD5 密钥
    @Schema(description = "MD5密钥")
    private String md5Key;

    /// V2 使用系统公私钥
    @Schema(description = "使用系统密钥")
    private Boolean useSystemKey;

    /// 商户 RSA 公钥
    @Schema(description = "商户RSA公钥")
    private String publicKey;
}
