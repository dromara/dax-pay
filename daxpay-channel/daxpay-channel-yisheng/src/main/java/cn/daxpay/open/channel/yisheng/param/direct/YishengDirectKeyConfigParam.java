package cn.daxpay.open.channel.yisheng.param.direct;

import cn.daxpay.open.platform.common.json.deserializer.CredentialKeepWhenBlankDeserializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import tools.jackson.databind.annotation.JsonDeserialize;

/// # 易生直连密钥配置保存参数
///
/// 以 channelMchNo 定位记录, 仅更新可编辑密钥字段(商户私钥/易生平台公钥);
/// partner/merchantId 为不可变身份字段, 创建时写入后不参与保存。
@Data
@Accessors(chain = true)
@Schema(title = "易生直连密钥配置保存参数")
public class YishengDirectKeyConfigParam {

    @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}")
    @Schema(description = "通道商户号")
    private String channelMchNo;

    @NotNull(message = "{validation.field.sandbox.notNull}")
    @Schema(description = "是否沙箱环境")
    private Boolean sandbox;

    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @Schema(description = "商户 RSA 私钥(Base64/PEM, PKCS8 与 PKCS1 均兼容)")
    private String merchantPrivateKey;

    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @Schema(description = "易生平台 RSA 公钥(Base64/PEM, X509)")
    private String platformPublicKey;
}
