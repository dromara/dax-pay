package cn.daxpay.open.channel.cpcn.param.direct;

import cn.daxpay.open.platform.common.json.deserializer.CredentialKeepWhenBlankDeserializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import tools.jackson.databind.annotation.JsonDeserialize;

/// # 中金直连密钥配置保存参数
///
/// 以 channelMchNo 定位记录, 仅更新可编辑字段(网关地址 + 指定分账子商户号 + 两份证书材料);
/// institutionId 为不可变身份字段, 创建时写入后不参与保存。
@Data
@Accessors(chain = true)
@Schema(title = "中金直连密钥配置保存参数")
public class CpcnDirectKeyConfigParam {

    @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}")
    @Schema(description = "通道商户号")
    private String channelMchNo;

    @NotNull(message = "{validation.field.sandbox.notNull}")
    @Schema(description = "是否沙箱环境")
    private Boolean sandbox;

    @Schema(description = "中金网关地址(开户开通后下发)")
    private String gatewayUrl;

    @Schema(description = "指定分账子商户号(可空, 多个用英文逗号分隔)")
    private String appointMerchantIds;

    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @Schema(description = "商户证书 pfx 内容(Base64, PKCS12)")
    private String merchantCertPfx;

    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @Schema(description = "商户证书密码")
    private String merchantCertPassword;

    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @Schema(description = "中金平台证书 cer 内容(Base64/PEM)")
    private String platformCert;
}
