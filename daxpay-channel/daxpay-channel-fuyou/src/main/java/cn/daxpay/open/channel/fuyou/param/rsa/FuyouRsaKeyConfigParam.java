package cn.daxpay.open.channel.fuyou.param.rsa;

import cn.daxpay.open.platform.common.json.deserializer.CredentialKeepWhenBlankDeserializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;
import tools.jackson.databind.annotation.JsonDeserialize;

/// # 富友 RSA 密钥对配置保存参数
@Data
@Accessors(chain = true)
@Schema(title = "富友 RSA 密钥对配置保存参数")
public class FuyouRsaKeyConfigParam {

    @NotBlank(message = "{validation.field.channelMchNo.notBlank}")
    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "富友商户号(mchnt_cd)")
    private String mchntCd;

    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @Schema(description = "商户RSA私钥(PKCS1/PKCS8, 加密存储, 为空时保留原值)")
    private String privateKey;

    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @Schema(description = "富友平台RSA公钥(X509, 加密存储, 为空时保留原值)")
    private String publicKey;
}
