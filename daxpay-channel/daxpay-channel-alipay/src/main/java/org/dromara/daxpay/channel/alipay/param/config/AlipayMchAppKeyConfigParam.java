package org.dromara.daxpay.channel.alipay.param.config;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 支付宝直连商户应用密钥配置保存参数
///
@Data
@Accessors(chain = true)
@Schema(title = "支付宝直连商户应用密钥配置保存参数")
public class AlipayMchAppKeyConfigParam {

    @NotNull(message = "{validation.field.id.notNull}")
    @Schema(description = "关联应用ID")
    private Long appId;

    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    @Schema(description = "商户号")
    private String mchNo;

    @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}")
    @Schema(description = "通道商户号")
    private String channelMchNo;

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
