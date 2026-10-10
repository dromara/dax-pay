package cn.daxpay.open.channel.alipay.param.direct;

import cn.daxpay.open.platform.common.json.deserializer.CredentialKeepWhenBlankDeserializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import tools.jackson.databind.annotation.JsonDeserialize;

/// # 支付宝直连商户应用密钥配置保存参数(商户端)
///
/// 商户端专用参数, 不含商户号(防越权: 商户号与通道商户号由控制器按服务端应用记录组装,
/// 对照运营端参数 [AlipayDirectAppKeyConfigParam])。
///
/// 密钥/证书字段留空即保留原值: 空串经 [CredentialKeepWhenBlankDeserializer] 归一为 null,
/// 由 convert 的空值不覆盖策略跳过更新。
@Data
@Accessors(chain = true)
@Schema(title = "支付宝直连商户应用密钥配置保存参数(商户端)")
public class MchAlipayDirectAppKeyConfigParam {

    @NotNull(message = "{validation.field.alipayDirectAppId.notNull}")
    @Schema(description = "关联应用ID")
    private Long alipayDirectAppId;

    @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}")
    @Schema(description = "通道商户号")
    private String channelMchNo;

    @NotBlank(message = "{validation.field.authType.notBlank}")
    @Schema(description = "认证类型")
    private String authType;

    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @Schema(description = "支付宝公钥")
    private String alipayPublicKey;

    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @Schema(description = "应用私钥")
    private String privateKey;

    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @Schema(description = "应用公钥证书")
    private String appCert;

    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @Schema(description = "支付宝公钥证书")
    private String alipayCert;

    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @Schema(description = "支付宝CA根证书")
    private String alipayRootCert;

    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @Schema(description = "AES通信密钥")
    private String secretKey;

    @NotNull(message = "{validation.field.sandbox.notNull}")
    @Schema(description = "是否沙箱环境")
    private Boolean sandbox;
}
