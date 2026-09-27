package cn.daxpay.open.channel.douyin.param.direct;

import cn.daxpay.open.platform.common.json.deserializer.CredentialKeepWhenBlankDeserializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;
import tools.jackson.databind.annotation.JsonDeserialize;

/// # 抖音直连密钥配置保存参数
///
/// 保存/更新抖音直连密钥时接收的请求参数，含通道商户号和密钥信息。
///
@Data
@Accessors(chain = true)
@Schema(title = "抖音直连密钥配置保存参数")
public class DouyinDirectKeyConfigParam {

    @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}")
    @Schema(description = "通道商户号")
    private String channelMchNo;

    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    @Schema(description = "商户号")
    private String mchNo;

    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @Schema(description = "商户私钥")
    private String merchantPrivateKey;

    @Schema(description = "商家公钥证书序列号")
    private String merchantSerialNumber;

    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @Schema(description = "接口加密密钥")
    private String encryptKey;
}
