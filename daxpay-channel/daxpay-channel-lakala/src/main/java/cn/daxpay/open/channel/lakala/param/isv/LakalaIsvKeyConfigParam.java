package cn.daxpay.open.channel.lakala.param.isv;

import cn.daxpay.open.platform.common.json.deserializer.CredentialKeepWhenBlankDeserializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import tools.jackson.databind.annotation.JsonDeserialize;

/// # 拉卡拉服务商密钥配置保存参数
///
@Data
@Accessors(chain = true)
@Schema(title = "拉卡拉服务商密钥配置保存参数")
public class LakalaIsvKeyConfigParam {

    @NotBlank(message = "{validation.field.product.notBlank}")
    @Schema(description = "产品编码")
    private String product;

    @NotBlank(message = "{validation.field.lklAppId.notBlank}")
    @Schema(description = "拉卡拉应用编号")
    private String lklAppId;

    @NotBlank(message = "{validation.field.mchSerialNo.notBlank}")
    @Schema(description = "商户证书序列号")
    private String mchSerialNo;

    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @Schema(description = "商户RSA私钥(加密存储)")
    private String privateKey;

    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @Schema(description = "拉卡拉RSA公钥(加密存储)")
    private String publicKey;

    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @Schema(description = "SM4密钥(加密存储)")
    private String sm4Key;

    @Schema(description = "机构代码")
    private String orgCode;

    @NotNull(message = "{validation.field.sandbox.notNull}")
    @Schema(description = "是否沙箱环境")
    private Boolean sandbox;
}
