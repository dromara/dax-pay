package cn.daxpay.open.channel.jeepay.param;

import cn.daxpay.open.platform.common.json.deserializer.CredentialKeepWhenBlankDeserializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;
import tools.jackson.databind.annotation.JsonDeserialize;

/// # Jeepay通道密钥配置保存参数
///
/// 以 channelMchNo(通道商户号) 作为唯一标识定位记录,
/// mchNo(平台商户号) 为不可变身份字段, 创建时写入后永不可改, 不参与保存;
/// 密钥字段为空时保留原值(不覆盖)。
@Data
@Accessors(chain = true)
@Schema(title = "Jeepay通道密钥配置保存参数")
public class JeepayKeyConfigParam {

    @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}")
    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "Jeepay平台网关地址")
    private String serverUrl;

    @Schema(description = "Jeepay商户号(mchNo)")
    private String jeepayMchNo;

    @Schema(description = "应用ID(appId)")
    private String appId;

    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @Schema(description = "应用密钥(appSecret, MD5签名钥)")
    private String appSecret;
}
