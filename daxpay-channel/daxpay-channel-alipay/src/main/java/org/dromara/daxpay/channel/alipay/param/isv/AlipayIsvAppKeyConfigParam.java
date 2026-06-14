package org.dromara.daxpay.channel.alipay.param.isv;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 支付宝服务商应用密钥配置保存参数
///
/// 保存/更新支付宝服务商应用密钥和证书时接收的请求参数，认证类型决定必填字段：公钥模式需传支付宝公钥，证书模式需传三个证书。
///
@Data
@Accessors(chain = true)
@Schema(title = "支付宝服务商应用密钥配置保存参数")
public class AlipayIsvAppKeyConfigParam {

    @NotNull(message = "{validation.field.id.notNull}")
    @Schema(description = "支付宝服务商应用ID")
    private Long appId;

    @NotBlank(message = "{validation.field.authType.notBlank}")
    @Schema(description = "认证类型")
    private String authType;

    @Schema(description = "支付宝公钥")
    private String alipayPublicKey;

    @Schema(description = "应用私钥")
    private String privateKey;

    @Schema(description = "应用公钥证书")
    private String appCert;

    @Schema(description = "支付宝公钥证书")
    private String alipayCert;

    @Schema(description = "支付宝CA根证书")
    private String alipayRootCert;

    @Schema(description = "AES通信密钥")
    private String secretKey;
}
