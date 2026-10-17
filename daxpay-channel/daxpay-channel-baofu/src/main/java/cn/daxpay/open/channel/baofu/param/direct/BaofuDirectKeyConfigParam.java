package cn.daxpay.open.channel.baofu.param.direct;

import cn.daxpay.open.platform.common.json.deserializer.CredentialKeepWhenBlankDeserializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import tools.jackson.databind.annotation.JsonDeserialize;

/// # 宝付直连密钥配置保存参数
///
/// 以 channelMchNo 定位记录, 仅更新可编辑密钥字段(两份证书 + 密码 + 聚合交易商户号);
/// merId/terId 为不可变身份字段, 创建时写入后不参与保存。
@Data
@Accessors(chain = true)
@Schema(title = "宝付直连密钥配置保存参数")
public class BaofuDirectKeyConfigParam {

    @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}")
    @Schema(description = "通道商户号")
    private String channelMchNo;

    @NotNull(message = "{validation.field.sandbox.notNull}")
    @Schema(description = "是否沙箱环境")
    private Boolean sandbox;

    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @Schema(description = "商户证书 pfx 内容(Base64, PKCS12)")
    private String merchantCertPfx;

    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @Schema(description = "商户证书密码")
    private String merchantCertPassword;

    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @Schema(description = "宝付平台证书 cer 内容(Base64/PEM)")
    private String platformCert;

    @Schema(description = "聚合交易商户号(subMchId, 在微信/支付宝报备的商户号)")
    private String subMchId;
}
