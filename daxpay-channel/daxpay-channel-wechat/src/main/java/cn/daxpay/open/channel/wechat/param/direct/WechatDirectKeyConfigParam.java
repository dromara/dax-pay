package cn.daxpay.open.channel.wechat.param.direct;

import cn.daxpay.open.platform.common.json.deserializer.CredentialKeepWhenBlankDeserializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;
import tools.jackson.databind.annotation.JsonDeserialize;

/// # 微信直连密钥配置保存参数
///
/// 保存/更新微信直连密钥和证书时接收的请求参数，含通道商户号和API V3密钥等信息。
///
@Data
@Accessors(chain = true)
@Schema(title = "微信直连密钥配置保存参数")
public class WechatDirectKeyConfigParam {

    @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}")
    @Schema(description = "通道商户号")
    private String channelMchNo;

    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    @Schema(description = "商户号")
    private String mchNo;

    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @Schema(description = "API V3密钥")
    private String apiKeyV3;

    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @Schema(description = "支付公钥")
    private String publicKey;

    @Schema(description = "支付公钥ID")
    private String publicKeyId;

    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @Schema(description = "商户私钥")
    private String privateKey;

    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @Schema(description = "商户证书")
    private String privateCert;

    @Schema(description = "证书序列号")
    private String certSerialNo;
}
